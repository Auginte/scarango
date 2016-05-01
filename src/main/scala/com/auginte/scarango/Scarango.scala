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
  *
  * Trait encapsulates common functions among all implementations of ArangoDB API wrappers
  *
  * @see [[ScarangoStreams]]
  * @see [[ScarangoFutures]]
  * @see [[ScarangoAwait]]
  */
trait Scarango {
  protected def implicits(context: Context) = (context.actorSystem, context.materializer, context.actorSystem.dispatcher, context)

  def toStreams: ScarangoStreams
  def toFutures: ScarangoFutures
  def toAwait: ScarangoAwait

  def withDatabase(newName: String): Scarango
  def withUser(user: User): Scarango

  def context: Context
}

/**
  * Common constructors for ArangoDB wrapper
  */
object Scarango {
  def newStreams(context: Context = Context.default) = new ScarangoStreams(context)
  def newFutures(context: Context = Context.default) = newStreams(context).toFutures
  def newAwait(context: Context = Context.default) = newFutures(context).toAwait
}

/**
  * Implementing ArangoDB API using reactive streams (flows)
  *
  * @param context current database, authetntication and other state parameters
  */
case class ScarangoStreams(context: Context = Context.default) extends Scarango {
  implicit val (_s, _m, _d, _c) = implicits(context)

  override def toStreams: ScarangoStreams = this
  override def toFutures: ScarangoFutures = new ScarangoFutures(this)
  override def toAwait: ScarangoAwait = new ScarangoAwait(toFutures)

  def withDatabase(newName: String) = new ScarangoStreams(context.withDatabase(newName))
  def withUser(user: User) = new ScarangoStreams(context.withAuthorisation(user))

  private def debugResponse(response: HttpResponse): HttpResponse = {
    response.entity.dataBytes.runFold(ByteString(""))(_ ++ _).map(_.utf8String).onComplete{
      case Success(s) => println(s)
      case Failure(e) => println("EXCEPTION: " +e)
    }
    response
  }

  //
  // API coverage
  //

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

/**
  * Implementing ArangoDB API using features (thread level)
  *
  * @param flows delegating current database, authentication and other state parameters
  */
case class ScarangoFutures(flows: ScarangoStreams) extends Scarango {
  def context = flows.context
  implicit val (_s, _m, _d, _c) = implicits(context)

  override def toStreams: ScarangoStreams = flows
  override def toFutures: ScarangoFutures = this
  override def toAwait: ScarangoAwait = new ScarangoAwait(toFutures)

  def withDatabase(newName: String) = new ScarangoFutures(flows.withDatabase(newName))
  def withUser(user: User) = new ScarangoFutures(flows.withUser(user))

  /** Future[Future[A]] comes from request + data chunk. We need documents per request */
  private def lower[A](data: Future[A]) = data

  //
  // API coverage
  //

  def version() = flows.version.runWith(Sink.head).flatMap(lower)

  def listDatabases() = flows.databases.runWith(Sink.head).flatMap(lower)

  def listCollections() = flows.collections.runWith(Sink.head).flatMap(lower)

  def create(database: cr.Database) = flows.create(database).runWith(Sink.head).flatMap(lower)

  def create(collection: cr.Collection) = flows.create(collection).runWith(Sink.head).flatMap(lower)

  def create(document: cr.Document) = flows.create(document).runWith(Sink.head).flatMap(lower)

  def query(all: All) = flows.query(all).runWith(Sink.head).flatMap(lower)

  def get(document: gt.Document) = flows.get(document).runWith(Sink.head).flatMap(lower)

  def iterator(all: All) = flows.iterator(all)

  def delete(database: dl.Database) = flows.delete(database).runWith(Sink.head).flatMap(lower)

  def delete(collection: dl.Collection) = flows.delete(collection).runWith(Sink.head).flatMap(lower)

  def delete(document: dl.Document) = flows.delete(document).runWith(Sink.head).flatMap(lower)
}

/**
  * Implementing ArangoDB API using blocked theads (will wait until result)
  *
  * @param futures delegating current database, authentication and other state parameters
  */
class ScarangoAwait(futures: ScarangoFutures) extends Scarango {
  def context = futures.context
  implicit val (_s, _m, _d, _c) = implicits(context)

  override def toStreams: ScarangoStreams = futures.flows
  override def toFutures: ScarangoFutures = futures
  override def toAwait: ScarangoAwait = this

  def withDatabase(newName: String) = new ScarangoAwait(futures.withDatabase(newName))
  def withUser(user: User) = new ScarangoAwait(futures.withUser(user))

  //
  // API coverage
  //

  def version() = Await.result(futures.version(), context.waitTime)

  def listDatabases() = Await.result(futures.listDatabases(), context.waitTime)

  def listCollections() = Await.result(futures.listCollections(), context.waitTime)

  def create(database: cr.Database) = Await.result(futures.create(database), context.waitTime)

  def create(collection: cr.Collection) = Await.result(futures.create(collection), context.waitTime)

  def create(document: cr.Document) = Await.result(futures.create(document), context.waitTime)

  def query(all: All) = Await.result(futures.query(all), context.waitTime)

  def get(document: gt.Document) = Await.result(futures.get(document), context.waitTime)

  def iterator(all: All) =
    Await.result(futures.iterator(all).flatMapConcat(Source.fromFuture).runWith(Sink.head).map(_.iterator), context.waitTime)

  def delete(database: dl.Database) = Await.result(futures.delete(database), context.waitTime)

  def delete(collection: dl.Collection) = Await.result(futures.delete(collection), context.waitTime)

  def delete(document: dl.Document) = Await.result(futures.delete(document), context.waitTime)
}
