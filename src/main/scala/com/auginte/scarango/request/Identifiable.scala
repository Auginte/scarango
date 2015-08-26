package com.auginte.scarango.request

import com.auginte.scarango.common.{Authorisation, StateParameters}
import spray.http.{HttpEntity, HttpMethod, Uri}

/**
 * Wrapper for request to add context or track between request and response.
 *
 * Responses are wrapped back into [[com.auginte.scarango.response.Identifiable]]
 */
case class Identifiable(
                         request: Request,
                         id: Any,
                         authorisation: Authorisation = Authorisation.default,
                         database: String = "_system")
  extends Request with StateParameters {

  override val method: HttpMethod = request.method
  override val uri: Uri = request.uri
  override val entity: HttpEntity = request.entity
  override protected val _authorisation: Authorisation = authorisation
}
