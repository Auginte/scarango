package com.auginte.scarango.get

import com.auginte.scarango.common.Authorisation
import spray.http._

/**
 * Common function for ArangoDb REST API request
 */
abstract class Request(val authorisation: Authorisation = Authorisation.default) {
  val method: HttpMethod = HttpMethods.GET
  val uri: Uri = Uri./
  val headers: List[HttpHeader] = List(authorisation.http)
  val entity: HttpEntity = HttpEntity.Empty

  def http = HttpRequest(method, uri, headers, entity)
}
