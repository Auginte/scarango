package com.auginte.scarango

import akka.http.javadsl.model.HttpEntities
import akka.http.scaladsl.model.{HttpMethods, HttpProtocols, HttpRequest}
import com.auginte.scarango.request.raw.create.{Collection, Database, Document}
import com.auginte.scarango.request.raw.query.simple.All
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
  private val put = HttpMethods.PUT

  def getVersion(implicit context: Context) = HttpRequest(get, "/_api/version", headers(context))

  def create(database: Database)(implicit context: Context) =
    HttpRequest(post, "/_api/database", headers(context), HttpEntities.create(database.toJson.compactPrint))

  def create(collection: Collection)(implicit context: Context) =
    HttpRequest(post, "/_api/collection", headers(context), HttpEntities.create(collection.toJson.compactPrint))

  def create(document: Document)(implicit context: Context) =
    HttpRequest(post, s"/_api/document?collection=${document.collectionName}", headers(context), HttpEntities.create(document.rawData))

  def query(all: All)(implicit context: Context) =
    HttpRequest(put, "/_api/simple/all", headers(context), HttpEntities.create(all.toJson.compactPrint))
}
