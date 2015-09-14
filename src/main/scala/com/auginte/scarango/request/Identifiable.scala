package com.auginte.scarango.request

import com.auginte.scarango.common.StateParameters
import com.auginte.scarango.request.parts.Authorisation
import com.auginte.scarango.state.{DatabaseName, DatabaseNames}
import spray.http.{HttpEntity, HttpMethod, Uri}

/**
 * Wrapper for request to add context or track between request and response.
 *
 * Responses are wrapped back into [[com.auginte.scarango.response.Identifiable]]
 */
case class Identifiable(
                         request: Request,
                         id: Any,
                         database: DatabaseName = DatabaseNames.default,
                         override val authorisation: Authorisation = Authorisation.default)
  extends Request with StateParameters {

  override val method: HttpMethod = request.method
  override val uri: Uri = request.uri
  override val entity: HttpEntity = request.entity
}
