package com.auginte.scarango.common

import spray.http._

/**
 * Common function for ArangoDb REST API request
 */
abstract class Request(val authorisation: Authorisation = Authorisation.default) {
  val method: HttpMethod
  val uri: Uri
  val headers: List[HttpHeader] = List(authorisation.http)
  val entity: HttpEntity

  def http = HttpRequest(method, uri, headers, entity)
}
