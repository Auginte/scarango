package com.auginte.scarango

import akka.http.scaladsl.model.HttpResponse
import akka.http.scaladsl.unmarshalling.Unmarshal

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
}
