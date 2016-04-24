package com.auginte.scarango

import akka.http.scaladsl.model.HttpResponse
import akka.stream.scaladsl._
import akka.util.ByteString
import com.auginte.scarango.request.raw.create.{Collection, Database, Document}
import com.auginte.scarango.request.raw.query.simple.All

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

    val databases = Source.single(request.listDatabases)
      .via(state.database)
      .map (response.toDatabases)

    def create(collection: Collection) = Source.single(request.create(collection))
      .via(state.database)
      .map(response.toCollectionCreated)

    def create(database: Database) = Source.single(request.create(database))
      .via(state.database)
      .map(response.toDatabaseCreated)

    def create(document: Document) = Source.single(request.create(document))
      .via(state.database)
      .map(response.toDocumentCreated)

    def query(all: All) = Source.single(request.query(all))
      .via(state.database)
      .map(response.toSimpleQueryResult)

    def iterator(all: All) = Source.single(request.query(all))
      .via(state.database)
      .map(response.toDocumentIterator)
  }

  object Futures {
    def version() = Flows.version.runWith(Sink.head).flatMap(lower)

    def listDatabases() = Flows.databases.runWith(Sink.head).flatMap(lower)

    def create(database: Database) = Flows.create(database).runWith(Sink.head).flatMap(lower)

    def create(collection: Collection) = Flows.create(collection).runWith(Sink.head).flatMap(lower)

    def create(document: Document) = Flows.create(document).runWith(Sink.head).flatMap(lower)

    def query(all: All) = Flows.query(all).runWith(Sink.head).flatMap(lower)

    def iterator(all: All) = Flows.iterator(all)
  }

  object Results {
    def version() = Await.result(Futures.version(), context.waitTime)

    def listDatabases() = Await.result(Futures.listDatabases(), context.waitTime)

    def create(database: Database) = Await.result(Futures.create(database), context.waitTime)

    def create(collection: Collection) = Await.result(Futures.create(collection), context.waitTime)

    def create(document: Document) = Await.result(Futures.create(document), context.waitTime)

    def query(all: All) = Await.result(Futures.query(all), context.waitTime)

    def iterator(all: All) =
      Await.result(Futures.iterator(all).flatMapConcat(Source.fromFuture).runWith(Sink.head).map(_.iterator), context.waitTime)
  }
}
