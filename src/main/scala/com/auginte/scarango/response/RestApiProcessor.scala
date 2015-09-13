package com.auginte.scarango.response

import com.auginte.scarango.request._
import com.auginte.scarango.response.raw._
import com.auginte.scarango.{request => r, response}
import spray.http.HttpResponse
import spray.json.DefaultJsonProtocol._
import spray.json.JsonParser

/**
 * Converts ArangoDB HTTP response to response objects
 */
private[scarango] object RestApiProcessor {
  def process(request: Request, httpResponse: HttpResponse): Response = {
    val entity = httpResponse.entity.asString
    request match {
      case r.Identifiable(r, id, authentication, database) =>
        val inner = process(r, httpResponse)
        response.Identifiable(inner, id, request, authentication, database)
      case GetVersion =>
        implicit val versionFormat = jsonFormat2(Version)
        JsonParser(entity).convertTo[Version]
      case ListDatabases =>
        implicit val format = jsonFormat3(DatabaseList)
        JsonParser(entity).convertTo[DatabaseList]
      case d: CreateDatabase =>
        val raw = boolResponse(entity)
        DatabaseCreated(d.name, raw)
      case d: RemoveDatabase =>
        val raw = boolResponse(entity)
        DatabaseRemoved(d, raw)
      case c: CreateCollection =>
        CollectionCreated(c.database, collectionData(entity))
      case c: GetCollection =>
        implicit val format = jsonFormat7(Collection)
        JsonParser(entity).convertTo[Collection]
      case c: RemoveCollection =>
        val raw = idResponse(entity)
        CollectionRemoved(c, raw)
      case c: CreateDocument =>
        DocumentCreated(c.database, documentData(entity))
      case c: ListDocuments =>
        DocumentList(documentsList(entity))
      case c: RemoveDocument =>
        DocumentRemoved(c.database, documentData(entity))
      case d: GetDocument =>
        Document(entity, d.id, d.database)
      case any =>
        RawResponse(httpResponse)
    }
  }

  private def boolResponse(entity: String): BoolResponse = {
    implicit val format = jsonFormat3(BoolResponse)
    JsonParser(entity).convertTo[BoolResponse]
  }

  private def idResponse(entity: String): IdResponse = {
    implicit val format = jsonFormat3(IdResponse)
    JsonParser(entity).convertTo[IdResponse]
  }

  private def collectionData(entity: String): RawCollectionCreated = {
    implicit val format = jsonFormat8(RawCollectionCreated)
    JsonParser(entity).convertTo[RawCollectionCreated]
  }

  private def documentData(entity: String): RawDocumentData = {
    implicit val format = jsonFormat4(RawDocumentData)
    JsonParser(entity).convertTo[raw.RawDocumentData]
  }

  private def documentsList(entity: String): RawDocuments = {
    implicit val format = jsonFormat1(RawDocuments)
    JsonParser(entity).convertTo[raw.RawDocuments]
  }
}
