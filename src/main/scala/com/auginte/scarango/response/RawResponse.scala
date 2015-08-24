package com.auginte.scarango.response

import spray.http.HttpResponse

/**
 * Generic response, when no Case classs/object is found by request type.
 *
 * See [[com.auginte.scarango.request.Request]]
 */
case class RawResponse(raw: HttpResponse) extends Response
