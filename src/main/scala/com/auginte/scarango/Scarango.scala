package com.auginte.scarango

import akka.actor.{Actor, ActorRef}
import akka.io.IO
import akka.io.Tcp.Close
import com.auginte.scarango.common.AkkaLogging
import com.auginte.scarango.errors.{ConnectionError, UnexpectedResponse}
import com.auginte.scarango.request.Request
import com.auginte.scarango.response.{ResponseIdentifier, RestApiProcessor}
import spray.can.Http
import spray.http.HttpResponse

import scala.collection.immutable.Queue

/**
 * Actor for ArrangoDB REST API.
 *
 * Routes messages and manages connection to ArrangoDB REST api.
 *
 * On error [[com.auginte.scarango.errors.UnprocessedRequest]] is returned and next request is processed.
 * It is up to client to handle error and resend.
 *
 * For other errors, [[com.auginte.scarango.errors.ScarangoError]] is returned to client.
 * For client actor errors, debug information is saved to `akka` logs.
 */
class Scarango extends Actor with AkkaLogging {
  private var connectionEstablished = false
  private var dbConnection: Option[ActorRef] = None
  private var queue: Queue[Packet] = Queue()

  // Main flow:
  //  User request     -> Queue -> Connection -> ArangoDB REST API
  //                               Connection <- ArangoDB REST API
  //                      Queue ---------------> ArangoDB REST API
  //  Response to user <- Queue <--------------- ArangoDB REST API
  //  User finishes                Connection -> ArangoDB REST API

  private case class Packet(client: ActorRef, request: Request)

  override def receive: Receive = {
    case r: Request =>
      debug("Saving request", r)
      requestToQueue(sender(), r)
      requestToDatabase()

    case Http.Connected(remote, local) =>
      debug("Connected to ArangoDB", local, remote)
      updateHost(sender())
      requestToDatabase()

    case raw: HttpResponse if raw.status.isSuccess =>
      debug("Received", raw.entity)
      responseToClient(raw)
      requestToDatabase()

    case raw: HttpResponse if raw.status.isFailure =>
      debug("Received error", raw.status)
      errorToClient(raw)
      requestToDatabase()

    case connectionData if queue.nonEmpty =>
      debug("Received unexpected", connectionData)
      errorToClient(connectionData)

    case other =>
      error("Unexpected state", other)
  }

  private def requestToQueue(client: ActorRef, request: Request): Unit = {
    queue = queue.enqueue(Packet(client, request))
    debug("Saved to queue", request, queue.size)
  }

  private def requestToDatabase(): Unit = dbConnection match {
    case Some(connection) if connectionEstablished => queue.dequeueOption match {
      case Some((packet, tail)) =>
        debug("Sending", packet.request)
        connection ! packet.request.http
      case None =>
        debug("Queue empty. Waiting")
    }
    case Some(connection) if !connectionEstablished =>
      debug("Still connecting... Keeping queue the same", connection)
    case None =>
      debug("Connecting to ArangoDB")
      dbConnection = Some(connect)
  }

  private def connect: ActorRef = {
    implicit val system = context.system
    val restApi = IO(Http)
    restApi ! Http.Connect("127.0.0.1", port = 8529)
    restApi
  }

  private def updateHost(connectionActor: ActorRef): Unit = {
    dbConnection = Some(connectionActor)
    connectionEstablished = true
  }

  private def responseToClient(raw: HttpResponse): Unit = queue.dequeueOption match {
    case Some((Packet(client, request), tail)) =>
      val parsed = RestApiProcessor.process(request, raw)
      debug("Parsed", parsed)
      client ! ResponseIdentifier(parsed, request)
      queue = tail
    case None =>
      error("No client to receive", raw)
  }

  private def errorToClient(raw: HttpResponse): Unit = queue.dequeueOption match {
    case Some((Packet(client, request), tail)) =>
      debug("Pass to client to handle", request)
      client ! UnexpectedResponse(raw, request)
      queue = tail
    case None =>
      error("No client to recover from ArangoDB API error", raw)
  }

  private def errorToClient(raw: Any): Unit = queue.dequeueOption match {
    case Some((Packet(client, request), tail)) =>
      debug("Pass to client to handle", request)
      client ! ConnectionError(raw, request)
      queue = tail
    case None =>
      error("No client to recover from ArangoDB connection error", raw)
  }

  @throws[Exception](classOf[Exception])
  override def postStop(): Unit = {
    dbConnection match {
      case Some(connection) if connectionEstablished =>
        debug("Closing ArangoDb connection")
        dbConnection.get ! Close
      case None =>
        debug("ArangoDb connection already closed")
    }
    super.postStop()
  }
}
