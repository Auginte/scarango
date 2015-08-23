package com.auginte.scarango.response

import com.auginte.scarango.common.Request
import com.auginte.scarango.response.raw.BoolResponse
import com.auginte.scarango.{create, get, remove}
import spray.http.HttpResponse
import spray.json.DefaultJsonProtocol._
import spray.json.JsonParser

/**
 * Converts ArangoDB HTTP response to response objects
 */
object RestApiProcessor {
  def process(request: Request, httpResponse: HttpResponse): ResponseData = request match {
    case get.Version =>
      implicit val versionFormat = jsonFormat2(Version)
      JsonParser(httpResponse.entity.asString).convertTo[Version]
    case get.Databases =>
      implicit val format = jsonFormat3(Databases)
      JsonParser(httpResponse.entity.asString).convertTo[Databases]
    case d: create.Database =>
      val raw = boolResponse(httpResponse)
      Database(d.name, raw)
    case d: remove.Database =>
      val raw = boolResponse(httpResponse)
      Removed(d, raw)
    case any =>
      RawResponse(httpResponse)
  }
  
  private def boolResponse(httpResponse: HttpResponse): BoolResponse = {
    implicit val format = jsonFormat3(BoolResponse)
    JsonParser(httpResponse.entity.asString).convertTo[BoolResponse]
  }
}
