package com.auginte.scarango

import akka.http.scaladsl.Http
import akka.http.scaladsl.marshallers.sprayjson.SprayJsonSupport
import akka.http.scaladsl.model.{HttpMethods, HttpRequest, HttpResponse}
import akka.http.scaladsl.unmarshalling.Unmarshal
import akka.stream.scaladsl._
import com.auginte.scarango.response.raw
import spray.json._

import scala.concurrent.Future

/**
  * Wrapper ArrangoDB REST API.
  */
class Scarango(context: Context = Context.default) {
  private val defaultPort = 8529

  implicit val system = context.actorSystem
  implicit val materializer = context.materializer
  implicit val executionContext = system.dispatcher

  val connectionFlow: Flow[HttpRequest, HttpResponse, Future[Http.OutgoingConnection]] = {
    val (host, port) = context.endpoint.split(":") match {
      case Array(h, p) => (h, p.toInt)
      case Array(h) => (h, defaultPort)
      case unknown => ("127.0.0.1", defaultPort)
    }
    Http(context.actorSystem).outgoingConnection(host, port, None)
  }

  trait JsonSupport extends SprayJsonSupport with DefaultJsonProtocol {
    implicit val versionFormat = jsonFormat2(raw.Version)
  }

  object MyJsonProtocol extends JsonSupport

  import MyJsonProtocol._

  private val versionFlow = {
    val request = HttpRequest(uri = "/_api/version", method = HttpMethods.GET, headers = List(context.authorisation.header))
    Source.single(request)
      .via(connectionFlow)
      .map { request =>
        Unmarshal(request.entity).to[raw.Version]
      }
  }

  def version(): Future[raw.Version] = {
    versionFlow.runWith(Sink.head).flatMap(inner => inner)
  }
}
