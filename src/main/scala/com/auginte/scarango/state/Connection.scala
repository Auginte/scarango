package com.auginte.scarango.state

import akka.http.scaladsl.Http
import akka.http.scaladsl.model.{HttpRequest, HttpResponse}
import akka.stream.scaladsl.Flow
import com.auginte.scarango.Context

import scala.concurrent.Future

/**
  * Wwapping connection to ArangoDB data
  */
private[state] class Connection(context: Context) {
  private val defaultPort = 8529

  val connectionFlow: Flow[HttpRequest, HttpResponse, Future[Http.OutgoingConnection]] = {
    val (host, port) = context.endpoint.split(":") match {
      case Array(h, p) => (h, p.toInt)
      case Array(h) => (h, defaultPort)
      case unknown => ("127.0.0.1", defaultPort)
    }
    Http(context.actorSystem).outgoingConnection(host, port, None)
  }
}
