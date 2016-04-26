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

  private def url(context: Context, path: String) =
    if (context.database != Context.defaultDatabase) s"/_db/${context.database}$path"
    else path

  def getVersion(implicit context: Context) = HttpRequest(get, "/_api/version", headers(context))

  def listDatabases(implicit context: Context) = HttpRequest(get, "/_api/database", headers(context))

  def listCollections(implicit context: Context) = HttpRequest(get, url(context, "/_api/collection"), headers(context))

  def create(database: Database)(implicit context: Context) =
    HttpRequest(post, "/_api/database", headers(context), HttpEntities.create(database.toJson.compactPrint))

  def create(collection: Collection)(implicit context: Context) =
    HttpRequest(post, url(context, "/_api/collection"), headers(context), HttpEntities.create(collection.toJson.compactPrint))

  def create(document: Document)(implicit context: Context) =
    HttpRequest(post, s"/_api/document?collection=${document.collectionName}", headers(context), HttpEntities.create(document.rawData))

  def query(all: All)(implicit context: Context) =
    HttpRequest(put, "/_api/simple/all", headers(context), HttpEntities.create(all.toJson.compactPrint))

  def delete(database: raw.delete.Database)(implicit context: Context) =
    HttpRequest(HttpMethods.DELETE, s"/_api/database/${database.name}", headers(context))

  def delete(collection: raw.delete.Collection)(implicit context: Context) =
    HttpRequest(HttpMethods.DELETE, url(context, s"/_api/collection/${collection.name}"), headers(context))
}
