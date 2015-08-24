package com.auginte.scarango.response

import com.auginte.scarango.common.Request
import com.auginte.scarango.response.created.{Collection, Database}
import com.auginte.scarango.response.existing.{Databases, Version}
import com.auginte.scarango.response.raw.{BoolResponse, IdResponse}
import com.auginte.scarango.{create, get, remove}
import spray.http.HttpResponse
import spray.json.DefaultJsonProtocol._
import spray.json.JsonParser

/**
 * Converts ArangoDB HTTP response to response objects
 */
object RestApiProcessor {
  def process(request: Request, httpResponse: HttpResponse): ResponseData = {
    val entity = httpResponse.entity.asString
    request match {
      case get.Version =>
        implicit val versionFormat = jsonFormat2(Version)
        JsonParser(entity).convertTo[Version]
      case get.Databases =>
        implicit val format = jsonFormat3(Databases)
        JsonParser(entity).convertTo[Databases]
      case d: create.Database =>
        val raw = boolResponse(entity)
        Database(d.name, raw)
      case d: remove.Database =>
        val raw = boolResponse(entity)
        removed.Database(d, raw)
      case _: create.Collection =>
        implicit val format = jsonFormat8(Collection)
        JsonParser(entity).convertTo[Collection]
      case c: remove.Collection =>
        val raw = idResponse(entity)
        removed.Collection(c, raw)
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
