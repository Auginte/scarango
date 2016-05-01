package com.auginte.scarango

import akka.http.scaladsl.model.HttpResponse
import akka.stream.scaladsl._
import akka.util.ByteString
import com.auginte.scarango.request.raw.create.User
import com.auginte.scarango.request.raw.{create => cr}
import com.auginte.scarango.request.raw.{delete => dl}
import com.auginte.scarango.request.raw.{get => gt}
import com.auginte.scarango.request.raw.query.simple.All

import scala.concurrent.{Await, Future}
import scala.util.{Failure, Success}

/**
  * Wrapper for ArangoDB REST API.
  */
case class Scarango(context: Context = Context.default) {
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

  def withDatabase(newName: String) = copy(context = context.withDatabase(newName))

  def withUser(user: User) = copy(context = context.withAuthorisation(user))

  object Flows {
    val version = Source.single(request.getVersion)
      .via(state.database)
      .map (response.toVersion)

    val databases = Source.single(request.listDatabases)
      .via(state.database)
      .map (response.toDatabases)

    val collections = Source.single(request.listCollections)
      .via(state.database)
      .map (response.toCollections)

    def create(collection: cr.Collection) = Source.single(request.create(collection))
      .via(state.database)
      .map(response.toCollectionCreated)

    def create(database: cr.Database) = Source.single(request.create(database))
      .via(state.database)
      .map(response.toDatabaseCreated)

    def create(document: cr.Document) = Source.single(request.create(document))
      .via(state.database)
      .map(response.toDocumentCreated)

    def query(all: All) = Source.single(request.query(all))
      .via(state.database)
      .map(response.toSimpleQueryResult)

    def get(document: gt.Document) = Source.single(request.get(document))
      .via(state.database)
      .map(response.toDocument)

    def iterator(all: All) = Source.single(request.query(all))
      .via(state.database)
      .map(response.toDocumentIterator)

    def delete(database: dl.Database) = Source.single(request.delete(database))
      .via(state.database)
      .map(response.toDatabaseDeleted)

    def delete(collection: dl.Collection) = Source.single(request.delete(collection))
      .via(state.database)
      .map(response.toCollectionDeleted)

    def delete(document: dl.Document) = Source.single(request.delete(document))
      .via(state.database)
      .map(response.toDocumentDeleted)
  }

  object Futures {
    def version() = Flows.version.runWith(Sink.head).flatMap(lower)

    def listDatabases() = Flows.databases.runWith(Sink.head).flatMap(lower)

    def listCollections() = Flows.collections.runWith(Sink.head).flatMap(lower)

    def create(database: cr.Database) = Flows.create(database).runWith(Sink.head).flatMap(lower)

    def create(collection: cr.Collection) = Flows.create(collection).runWith(Sink.head).flatMap(lower)

    def create(document: cr.Document) = Flows.create(document).runWith(Sink.head).flatMap(lower)

    def query(all: All) = Flows.query(all).runWith(Sink.head).flatMap(lower)

    def get(document: gt.Document) = Flows.get(document).runWith(Sink.head).flatMap(lower)

    def iterator(all: All) = Flows.iterator(all)

    def delete(database: dl.Database) = Flows.delete(database).runWith(Sink.head).flatMap(lower)

    def delete(collection: dl.Collection) = Flows.delete(collection).runWith(Sink.head).flatMap(lower)

    def delete(document: dl.Document) = Flows.delete(document).runWith(Sink.head).flatMap(lower)
  }

  object Results {
    def version() = Await.result(Futures.version(), context.waitTime)

    def listDatabases() = Await.result(Futures.listDatabases(), context.waitTime)

    def listCollections() = Await.result(Futures.listCollections(), context.waitTime)

    def create(database: cr.Database) = Await.result(Futures.create(database), context.waitTime)

    def create(collection: cr.Collection) = Await.result(Futures.create(collection), context.waitTime)

    def create(document: cr.Document) = Await.result(Futures.create(document), context.waitTime)

    def query(all: All) = Await.result(Futures.query(all), context.waitTime)

    def get(document: gt.Document) = Await.result(Futures.get(document), context.waitTime)

    def iterator(all: All) =
      Await.result(Futures.iterator(all).flatMapConcat(Source.fromFuture).runWith(Sink.head).map(_.iterator), context.waitTime)

    def delete(database: dl.Database) = Await.result(Futures.delete(database), context.waitTime)

    def delete(collection: dl.Collection) = Await.result(Futures.delete(collection), context.waitTime)

    def delete(document: dl.Document) = Await.result(Futures.delete(document), context.waitTime)
  }
}
