package com.auginte.scarango

import akka.http.scaladsl.model.HttpResponse
import akka.stream.scaladsl._
import akka.util.ByteString
import com.auginte.scarango.request.raw.create.{Collection, Document}

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

    def create(collection: Collection) = Source.single(request.create(collection))
      .via(state.database)
      .map(response.toCollectionCreated)

    def create(document: Document) = Source.single(request.create(document))
      .via(state.database)
      .map(response.toDocumentCreated)
  }

  object Futures {
    def version() = Flows.version.runWith(Sink.head).flatMap(lower)

    def create(collection: Collection) = Flows.create(collection).runWith(Sink.head).flatMap(lower)

    def create(document: Document) = Flows.create(document).runWith(Sink.head).flatMap(lower)
  }

  object Results {
    def version() = Await.result(Futures.version(), context.waitTime)

    def create(collection: Collection) = Await.result(Futures.create(collection), context.waitTime)

    def create(document: Document) = Await.result(Futures.create(document), context.waitTime)
  }
}
