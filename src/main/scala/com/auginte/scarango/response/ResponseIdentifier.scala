package com.auginte.scarango.response

import com.auginte.scarango.request.Request

/**
 * Wrapping response data and request.
 *
 * So client could better organise requests and responses
 * @deprecated Not used in practice. Will be replaced with Identifiable
 */
case class ResponseIdentifier(data: Response, request: Request)
