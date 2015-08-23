package com.auginte.scarango.response

import com.auginte.scarango.common.Request
import com.auginte.scarango.{create, get}
import com.auginte.scarango.response.raw.BoolResponse
import spray.http.HttpResponse
import spray.json.DefaultJsonProtocol._
import spray.json.JsonParser

/**
 * Converts ArangoDB HTTP response to response objects
 */
object RestApiProcessor {
  def process(request: Request, rawResponse: HttpResponse): ResponseData = request match {
    case get.Version =>
      implicit val versionFormat = jsonFormat2(Version)
      JsonParser(rawResponse.entity.asString).convertTo[Version]
    case get.Databases =>
      implicit val format = jsonFormat3(Databases)
      JsonParser(rawResponse.entity.asString).convertTo[Databases]
    case d: create.Database =>
      implicit val format = jsonFormat3(BoolResponse)
      val raw = JsonParser(rawResponse.entity.asString).convertTo[BoolResponse]
      Database(d.name, raw)
    case any =>
      RawResponse(rawResponse)
  }
}
