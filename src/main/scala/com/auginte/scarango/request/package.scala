package com.auginte.scarango

import akka.http.javadsl.model.HttpEntities
import akka.http.scaladsl.model.{HttpMethods, HttpProtocols, HttpRequest}
import com.auginte.scarango.request.raw.create.{Collection, Document}
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

  def create(collection: Collection)(implicit context: Context) =
    HttpRequest(post, "/_api/collection", headers(context), HttpEntities.create(collection.toJson.compactPrint))

  def create(document: Document)(implicit context: Context) =
    HttpRequest(post, s"/_api/document?collection=${document.collectionName}", headers(context), HttpEntities.create(document.rawData))
}
