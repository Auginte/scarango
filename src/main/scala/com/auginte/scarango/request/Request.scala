package com.auginte.scarango.request

import com.auginte.scarango.common.Authorisation
import spray.http._

/**
 * Common function for ArangoDb REST API request
 */
abstract class Request {
  protected val _authorisation: Authorisation = Authorisation.default

  val method: HttpMethod
  val uri: Uri
  final lazy val headers: List[HttpHeader] = List(_authorisation.http)
  val entity: HttpEntity

  def http = HttpRequest(method, uri, headers, entity)
}
