package com.auginte.scarango

import akka.http.scaladsl.model.HttpResponse
import akka.stream.scaladsl._
import akka.util.ByteString
import com.auginte.scarango.request.raw.create.Collection

import scala.concurrent.{Await, Future}
import scala.util.{Failure, Success}

/**
  * Wrapper for ArrangoDB REST API.
  */
class Scarango(val context: Context = Context.default) {
  implicit val system = context.actorSystem
  implicit val materializer = context.materializer
  implicit val executionContext = system.dispatcher
  implicit val currentContext = context

  /** Future[Future[A]] comes from request + data chunk. We need documents per request */
  private def lower[A](data: Future[A]) = data

  private def debugResponse(response: HttpResponse): HttpResponse = {
    response.entity.dataBytes.runFold(ByteString(""))(_ ++ _).map(_.utf8String).onComplete{
      case Success(s) => println(s)
      case Failure(e) => println("EXCEPTION: " +e)
    }
    response
  }

  object Flows {
    val version = Source.single(request.getVersion)
      .via(state.database)
      .map (response.toVersion)

    def createCollection(collection: Collection) = Source.single(request.createCollection(collection))
      .via(state.database)
      .map(response.toCollectionCreated)
  }

  object Futures {
    def version() = Flows.version.runWith(Sink.head).flatMap(lower)

    def createCollection(collection: Collection) = Flows.createCollection(collection).runWith(Sink.head).flatMap(lower)
  }

  object Results {
    def version() = Await.result(Futures.version(), context.waitTime)

    def create(collection: Collection) = Await.result(Futures.createCollection(collection), context.waitTime)
  }
}
