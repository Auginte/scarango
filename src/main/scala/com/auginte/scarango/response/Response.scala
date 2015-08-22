package com.auginte.scarango.response

import com.auginte.scarango.get.Request

/**
 * Wrapping response data and request.
 *
 * So client could better organise requests and responses
 */
case class Response(data: ResponseData, request: Request)
