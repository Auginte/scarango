package com.auginte.scarango

import akka.http.scaladsl.model.HttpResponse
import akka.http.scaladsl.unmarshalling.Unmarshal
import akka.util.ByteString

import scala.concurrent.Future

/**
  * Converting responses from ArangoDB to Wrapped objects
  */
package object response {
  object MyJsonProtocol extends JsonSupport
  import MyJsonProtocol._

  def toVersion(implicit context: Context): (HttpResponse) => Future[raw.Version] = { response =>
    implicit val system = context.actorSystem
    implicit val materializer = context.materializer
    implicit val executionContext = system.dispatcher

    Unmarshal(response.entity).to[raw.Version]
  }

  def toCollectionCreated(implicit context: Context): (HttpResponse) => Future[raw.create.Collection] = { response =>
    implicit val system = context.actorSystem
    implicit val materializer = context.materializer
    implicit val executionContext = system.dispatcher

    Unmarshal(response.entity).to[raw.create.Collection]
  }

  def toDocumentCreated(implicit context: Context): (HttpResponse) => Future[raw.create.Document] = { response =>
    implicit val system = context.actorSystem
    implicit val materializer = context.materializer
    implicit val executionContext = system.dispatcher

    Unmarshal(response.entity).to[raw.create.Document]
  }

  def toRaw(implicit context: Context): (HttpResponse) => Future[String] = { response =>
    implicit val system = context.actorSystem
    implicit val materializer = context.materializer
    implicit val executionContext = system.dispatcher

    response.entity.dataBytes.runFold(ByteString(""))(_ ++ _).map(_.utf8String)
  }
}
