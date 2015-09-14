package com.auginte.scarango.request

import com.auginte.scarango.request.parts.Authorisation
import spray.http._

/**
 * Common function for ArangoDb REST API request
 */
abstract class Request {
  protected val authorisation: Authorisation = Authorisation.default

  val method: HttpMethod
  val uri: Uri
  final lazy val headers: List[HttpHeader] = List(authorisation.http)
  val entity: HttpEntity

  def http = HttpRequest(method, uri, headers, entity)
}
