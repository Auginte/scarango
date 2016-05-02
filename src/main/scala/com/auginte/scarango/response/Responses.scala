package com.auginte.scarango.response

import akka.http.scaladsl.model.{HttpResponse, ResponseEntity, StatusCodes}
import akka.http.scaladsl.unmarshalling.Unmarshaller
import akka.util.ByteString
import com.auginte.scarango.{Context, errors}

import scala.concurrent.Future
import scala.util.{Failure, Success, Try}

/**
  * Converting responses from ArangoDB to Wrapped objects
  *
  * It is easier to debug, when functions are in real object, not package object
  */
object Responses {
  object MyJsonProtocol extends JsonSupport
  import MyJsonProtocol._

  private def implicits(context: Context) = (context.actorSystem, context.materializer, context.actorSystem.dispatcher)

  private object Converter {
    def apply(value: HttpResponse)(implicit context: Context): Converter = new Converter(value, context)
  }

  private class Converter(val response: HttpResponse, context: Context) {
    implicit val executionContext = context.actorSystem.dispatcher
    implicit val errorContext = context

    private def now[T](data: T) = Future.successful(data)

    def to[B](implicit um: Unmarshaller[ResponseEntity, B]): Future[Try[B]] = {
      response.status match {
        case StatusCodes.Forbidden => now(Failure(errors.Forbidden(response)))
        case StatusCodes.NotFound => now(Failure(errors.NotFound(response)))
        case StatusCodes.BadRequest => now(Failure(errors.BadRequest(response)))
        case _ => um(response.entity)(context.actorSystem.dispatcher, context.materializer)
          .map(Success(_)).recover {
          case e: Exception => Failure(errors.Unexpected(e, response))
        }
      }
    }
  }


  def toVersion(implicit context: Context): (HttpResponse) => Future[Try[raw.Version]] = { response =>
    Converter(response).to[raw.Version]
  }

  def toDatabaseCreated(implicit context: Context): (HttpResponse) => Future[Try[raw.create.Database]] = { response =>
    Converter(response).to[raw.create.Database]
  }

  def toCollectionCreated(implicit context: Context): (HttpResponse) => Future[Try[raw.create.Collection]] = { response =>
    Converter(response).to[raw.create.Collection]
  }

  def toDocumentCreated(implicit context: Context): (HttpResponse) => Future[Try[raw.create.Document]] = { response =>
    Converter(response).to[raw.create.Document]
  }

  def toSimpleQueryResult(implicit context: Context): (HttpResponse) => Future[Try[raw.query.simple.All]] = { response =>
    Converter(response).to[raw.query.simple.All]
  }

  def toDocumentIterator(implicit context: Context): (HttpResponse) => Future[Try[Iterator[raw.query.simple.Document]]] = { response =>
    implicit val (s, m, e) = implicits(context)
    Converter(response).to[raw.query.simple.All].map(_.map(_.result.toIterator))
  }

  def toDocument(implicit context: Context): (HttpResponse) => Future[Try[raw.query.simple.Document]] = { response =>
    Converter(response).to[raw.query.simple.Document]
  }

  def toDatabases(implicit context: Context): (HttpResponse) => Future[Try[raw.list.Databases]] = { response =>
    Converter(response).to[raw.list.Databases]
  }

  def toCollections(implicit context: Context): (HttpResponse) => Future[Try[raw.list.Collections]] = { response =>
    Converter(response).to[raw.list.Collections]
  }

  def toDatabaseDeleted(implicit context: Context): (HttpResponse) => Future[Try[raw.delete.Database]] = { response =>
    Converter(response).to[raw.delete.Database]
  }

  def toCollectionDeleted(implicit context: Context): (HttpResponse) => Future[Try[raw.delete.Collection]] = { response =>
    Converter(response).to[raw.delete.Collection]
  }

  def toDocumentDeleted(implicit context: Context): (HttpResponse) => Future[Try[raw.delete.Document]] = { response =>
    Converter(response).to[raw.delete.Document]
  }

  def toRaw(implicit context: Context): (HttpResponse) => Future[String] = { response =>
    implicit val (s, m, e) = implicits(context)
    response.entity.dataBytes.runFold(ByteString(""))(_ ++ _).map(_.utf8String)
  }
}
