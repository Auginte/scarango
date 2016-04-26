package com.auginte.scarango

import akka.http.scaladsl.model.HttpResponse
import akka.http.scaladsl.unmarshalling.Unmarshal
import akka.util.ByteString

import scala.concurrent.{Await, Future}
import scala.util.{Failure, Success}

/**
  * Converting responses from ArangoDB to Wrapped objects
  */
package object response {
  object MyJsonProtocol extends JsonSupport
  import MyJsonProtocol._

  private def implicits(context: Context) = (context.actorSystem, context.materializer, context.actorSystem.dispatcher)

  def toVersion(implicit context: Context): (HttpResponse) => Future[raw.Version] = { response =>
    implicit val (s, m, e) = implicits(context)
    Unmarshal(response.entity).to[raw.Version]
  }

  def toDatabaseCreated(implicit context: Context): (HttpResponse) => Future[raw.create.Database] = { response =>
    implicit val (s, m, e) = implicits(context)
    Unmarshal(response.entity).to[raw.create.Database]
  }

  def toCollectionCreated(implicit context: Context): (HttpResponse) => Future[raw.create.Collection] = { response =>
    implicit val (s, m, e) = implicits(context)
    Unmarshal(response.entity).to[raw.create.Collection]
  }

  def toDocumentCreated(implicit context: Context): (HttpResponse) => Future[raw.create.Document] = { response =>
    implicit val (s, m, e) = implicits(context)
    Unmarshal(response.entity).to[raw.create.Document]
  }

  def toSimpleQueryResult(implicit context: Context): (HttpResponse) => Future[raw.query.simple.All] = { response =>
    implicit val (s, m, e) = implicits(context)
    Unmarshal(response.entity).to[raw.query.simple.All]
  }

  def toDocumentIterator(implicit context: Context): (HttpResponse) => Future[List[raw.query.simple.Document]] = { response =>
    implicit val (s, m, e) = implicits(context)
    Unmarshal(response.entity).to[raw.query.simple.All].map(_.result)
  }

  def toDatabases(implicit context: Context): (HttpResponse) => Future[raw.list.Databases] = { response =>
    implicit val (s, m, e) = implicits(context)
    Unmarshal(response.entity).to[raw.list.Databases]
  }

  def toCollections(implicit context: Context): (HttpResponse) => Future[raw.list.Collections] = { response =>
    implicit val (s, m, e) = implicits(context)
    Unmarshal(response.entity).to[raw.list.Collections]
  }

  def toDatabaseDeleted(implicit context: Context): (HttpResponse) => Future[raw.delete.Database] = { response =>
    implicit val (s, m, e) = implicits(context)
    Unmarshal(response.entity).to[raw.delete.Database]
  }

  def toCollectionDeleted(implicit context: Context): (HttpResponse) => Future[raw.delete.Collection] = { response =>
    implicit val (s, m, e) = implicits(context)
    Unmarshal(response.entity).to[raw.delete.Collection]
  }

  def toDocumentDeleted(implicit context: Context): (HttpResponse) => Future[raw.delete.Document] = { response =>
    implicit val (s, m, e) = implicits(context)
    Unmarshal(response.entity).to[raw.delete.Document]
  }

  def toRaw(implicit context: Context): (HttpResponse) => Future[String] = { response =>
    implicit val (s, m, e) = implicits(context)
    response.entity.dataBytes.runFold(ByteString(""))(_ ++ _).map(_.utf8String)
  }
}
