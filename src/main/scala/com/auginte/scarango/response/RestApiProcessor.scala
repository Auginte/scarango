package com.auginte.scarango.response

import com.auginte.scarango.request._
import com.auginte.scarango.response.raw.{BoolResponse, IdResponse}
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
      case GetDatabases =>
        implicit val format = jsonFormat3(Databases)
        JsonParser(entity).convertTo[Databases]
      case d: CreateDatabase =>
        val raw = boolResponse(entity)
        DatabaseCreated(d.name, raw)
      case d: RemoveDatabase =>
        val raw = boolResponse(entity)
        DatabaseRemoved(d, raw)
      case _: CreateCollection =>
        implicit val format = jsonFormat8(CollectionCreated)
        JsonParser(entity).convertTo[CollectionCreated]
      case c: RemoveCollection =>
        val raw = idResponse(entity)
        CollectionRemoved(c, raw)
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
}
