package com.auginte.scarango

import akka.NotUsed
import akka.http.scaladsl.model.{HttpRequest, HttpResponse}
import akka.stream.scaladsl._
import akka.util.ByteString
import com.auginte.scarango.request.Requests
import com.auginte.scarango.request.raw.create.User
import com.auginte.scarango.request.raw.{create => cr}
import com.auginte.scarango.request.raw.{delete => dl}
import com.auginte.scarango.request.raw.{get => gt}
import com.auginte.scarango.request.raw.query.simple.All
import com.auginte.scarango.response.Responses
import com.auginte.scarango.response.raw.query.simple.Document

import scala.concurrent.{Await, Future}
import scala.util.{Failure, Success, Try}

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

  private def source[T](element: T): Source[T, NotUsed] = Source.single(element)

  private def flow[T](input: HttpRequest, converter: (HttpResponse) => Future[T]) =
    source(input).via(state.database).map (converter)


  //
  // API coverage
  //

  val version =  flow(Requests.getVersion, Responses.toVersion)

  val databases = flow(Requests.listDatabases, Responses.toDatabases)

  val collections = flow(Requests.listCollections, Responses.toCollections)

  def create(collection: cr.Collection) = flow(Requests.create(collection), Responses.toCollectionCreated)

  def create(database: cr.Database) = flow(Requests.create(database), Responses.toDatabaseCreated)

  def create(document: cr.Document) = flow(Requests.create(document), Responses.toDocumentCreated)

  def query(all: All) = flow(Requests.query(all), Responses.toSimpleQueryResult)

  def get(document: gt.Document) = flow(Requests.get(document), Responses.toDocument)

  def iterator(all: All) = flow(Requests.query(all), Responses.toDocumentIterator)

  def delete(database: dl.Database) = flow(Requests.delete(database), Responses.toDatabaseDeleted)

  def delete(collection: dl.Collection) = flow(Requests.delete(collection), Responses.toCollectionDeleted)

  def delete(document: dl.Document) = flow(Requests.delete(document), Responses.toDocumentDeleted)
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

  def iterator(all: All) = flows.iterator(all).runWith(Sink.head).flatMap(lower)

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

  private def await[T](future: Future[Try[T]]): T = Await.result(future, context.waitTime) match {
    case Success(data) => data
    case Failure(f) => throw f
  }

  //
  // API coverage
  //

  def version() = await(futures.version())

  def listDatabases() = await(futures.listDatabases())

  def listCollections() = await(futures.listCollections())

  def create(database: cr.Database) = await(futures.create(database))

  def create(collection: cr.Collection) = await(futures.create(collection))

  def create(document: cr.Document) = await(futures.create(document))

  def query(all: All) = await(futures.query(all))

  def get(document: gt.Document) = await(futures.get(document))

  def iterator(all: All) = await[Iterator[Document]](futures.iterator(all))

  def delete(database: dl.Database) = await(futures.delete(database))

  def delete(collection: dl.Collection) = await(futures.delete(collection))

  def delete(document: dl.Document) = await(futures.delete(document))
}
