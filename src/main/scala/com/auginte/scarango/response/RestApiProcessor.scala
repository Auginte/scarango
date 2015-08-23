package com.auginte.scarango.response

import com.auginte.scarango.get
import com.auginte.scarango.get.Request
import spray.http.HttpResponse
import spray.json.DefaultJsonProtocol._
import spray.json.JsonParser

/**
 * Converts ArangoDB HTTP response to response objects
 */
object RestApiProcessor {
  def process(request: Request, rawResponse: HttpResponse): ResponseData = request match {
    case _: get.Version =>
      implicit val versionFormat = jsonFormat2(Version)
      JsonParser(rawResponse.entity.asString).convertTo[Version]
    case _: get.Databases =>
      implicit val format = jsonFormat3(Databases)
      JsonParser(rawResponse.entity.asString).convertTo[Databases]
    case any =>
      RawResponse(rawResponse)
  }
}
