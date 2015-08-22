package com.auginte.scarango

import akka.actor.{Actor, ActorRef}
import akka.io.IO
import akka.io.Tcp.{Close, ErrorClosed}
import com.auginte.scarango.common.AkkaLogging
import com.auginte.scarango.errors.{DatabaseClosed, UnexpectedDbResponse, UnexpectedRequest}
import com.auginte.scarango.response.{Response, RestApiProcessor}
import spray.can.Http
import spray.http.HttpResponse

/**
 * ArangoDatabase wrapper.
 *
 * Routes messags and manages connection to ArrangoDB REST api
 */
class Scarango extends Actor with AkkaLogging {
  private var dbConnection: Option[ActorRef] = None
  private var processing: Option[Packet] = None

  private case class Packet(client: ActorRef, request: get.Request)

  override def receive: Receive = {
    // Supported requests
    case r: get.Request =>
      debug("Saving request", r)
      request(sender(), r)

    // Low level HTTP connection to ArangoDB REST API
    case Http.Connected(remote, local) => processing match {
      case Some(Packet(client, request)) =>
        debug("Connection established. Sending", local, remote, request.uri, request)
        sender() ! request.http
      case None =>
        error("Connection established, but no requests to process")
    }
    case raw: HttpResponse if raw.status.isSuccess && processing.isDefined =>
      debug("Response from ArangoDb. Parsing", raw)
      processResponse(raw, processing.get)
      debug("Closing ArangoDb connection")
      sender() ! Close
    case e: ErrorClosed =>
      dbConnection = None
      processing match {
        case Some(Packet(client, message)) =>
          client ! DatabaseClosed(e, message)
        case None =>
          error("Connection closed and no client attached", e)
      }
    case raw: Any if processing.isDefined && sender() == processing.get.client =>
      sender() ! UnexpectedRequest(raw, processing.get.request)
    case raw: Any if processing.isDefined =>
      processing.get.client ! UnexpectedDbResponse(raw, processing.get.request)
      processing = None
    case raw: Any =>
      error("Unexpected state and no client attached. Passing object instead of instance?", raw)
  }

  /**
   * Save current request as processing, so result could returned to original client.
   *
   * Creates new connection or sends packet immediately if already connected to ArangoDB REST API.
   */
  private def request(client: ActorRef, message: get.Request): Unit = {
    processing = Some(Packet(client, message))
    dbConnection match {
      case Some(connection) =>
        debug("Connected. Sending now", message)
        connection ! message // Wait for HttpResponse
      case None =>
        val connection = connect
        debug("First connection. Connecting", connection)
        dbConnection = Some(connection) // Wait for Http.Connected
    }
  }

  /**
   * Connection to default ArangoDB REST API instance
   */
  private def connect: ActorRef = {
    implicit val system = context.system
    val restApi = IO(Http)
    restApi ! Http.Connect("127.0.0.1", port = 8529)
    restApi
  }

  private def processResponse(raw: HttpResponse, packet: Packet) = {
    val parsed = RestApiProcessor.process(packet.request, raw)
    debug("Parsed response object", parsed)
    packet.client ! Response(parsed, packet.request)
  }
}
