package com.auginte.scarango.response

import com.auginte.scarango.common.StateParameters
import com.auginte.scarango.request.Request
import com.auginte.scarango.request.parts.Authorisation
import com.auginte.scarango.state.DatabaseName

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
                         database: DatabaseName) extends Response with StateParameters
