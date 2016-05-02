package com.auginte.scarango.request

import akka.http.javadsl.model.HttpEntities
import akka.http.scaladsl.model.{HttpMethods, HttpRequest}
import com.auginte.scarango.Context
import com.auginte.scarango.request.raw.create.{Collection, Database}
import com.auginte.scarango.request.raw.{create => c}
import com.auginte.scarango.request.raw.{get => g}
import com.auginte.scarango.request.raw.query.simple.All
import spray.json._

/**
  * Named request queries
  *
  * It is easier to debug, when functions are in real object, not package object
  */
object Requests {
  object RequestJsonProtocol extends JsonSupport
  import RequestJsonProtocol._

  private def headers(context: Context) = List(context.authorisation.header)
  private val httpGet = HttpMethods.GET
  private val httpPost = HttpMethods.POST
  private val httpPut = HttpMethods.PUT
  private val httpDelete = HttpMethods.DELETE

  private def url(context: Context, path: String) =
    if (context.database != Context.defaultDatabase) s"/_db/${context.database}$path"
    else path

  def getVersion(implicit context: Context) = HttpRequest(httpGet, "/_api/version", headers(context))

  def listDatabases(implicit context: Context) = HttpRequest(httpGet, "/_api/database", headers(context))

  def listCollections(implicit context: Context) = HttpRequest(httpGet, url(context, "/_api/collection"), headers(context))

  def create(database: Database)(implicit context: Context) =
    HttpRequest(httpPost, "/_api/database", headers(context), HttpEntities.create(database.toJson.compactPrint))

  def create(collection: Collection)(implicit context: Context) =
    HttpRequest(httpPost, url(context, "/_api/collection"), headers(context), HttpEntities.create(collection.toJson.compactPrint))

  def create(document: c.Document)(implicit context: Context) =
    HttpRequest(httpPost, s"/_api/document?collection=${document.collectionName}", headers(context), HttpEntities.create(document.rawData))

  def query(all: All)(implicit context: Context) =
    HttpRequest(httpPut, url(context, "/_api/simple/all"), headers(context), HttpEntities.create(all.toJson.compactPrint))

  def get(document: g.Document)(implicit context: Context) =
    HttpRequest(httpGet, url(context, s"/_api/document/${document.collectionName}/${document.key}"), headers(context))


  def delete(database: raw.delete.Database)(implicit context: Context) =
    HttpRequest(httpDelete, s"/_api/database/${database.name}", headers(context))

  def delete(collection: raw.delete.Collection)(implicit context: Context) =
    HttpRequest(httpDelete, url(context, s"/_api/collection/${collection.name}"), headers(context))

  def delete(document: raw.delete.Document)(implicit context: Context) =
    HttpRequest(httpDelete, url(context, s"/_api/document/${document.collectionName}/${document.key}"), headers(context))
}
