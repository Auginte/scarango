package com.auginte.scarango

import akka.http.javadsl.model.HttpEntities
import akka.http.scaladsl.model.{HttpMethods, HttpProtocols, HttpRequest}
import com.auginte.scarango.request.raw.create.Collection
import spray.json._

/**
  * Named request queries
  */
package object request {
  object RequestJsonProtocol extends request.JsonSupport
  import RequestJsonProtocol._

  private def headers(context: Context) = List(context.authorisation.header)
  private val get = HttpMethods.GET
  private val post = HttpMethods.POST

  def getVersion(implicit context: Context) = HttpRequest(get, "/_api/version", headers(context))

  def createCollection(collection: Collection)(implicit context: Context) =
    HttpRequest(post, "/_api/collection", headers(context), HttpEntities.create(collection.toJson.compactPrint))
}
