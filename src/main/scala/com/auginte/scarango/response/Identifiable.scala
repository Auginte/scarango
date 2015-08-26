package com.auginte.scarango.response

import com.auginte.scarango.common.{Authorisation, StateParameters}
import com.auginte.scarango.request.Request

/**
 * Wrapping response data and request.
 *
 * So client could better track and organise requests and responses
 *
 * See [[com.auginte.scarango.request.Identifiable]]
 */
case class Identifiable(
                         data: Response,
                         id: Any,
                         request: Request,
                         authorisation: Authorisation,
                         database: String) extends Response with StateParameters
