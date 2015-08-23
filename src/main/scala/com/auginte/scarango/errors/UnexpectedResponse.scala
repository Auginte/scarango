package com.auginte.scarango.errors

import com.auginte.scarango.common.Request
import spray.http.HttpResponse

/**
 * Not expected response from ArangoDB REST API. For example: not authorised, bad URL, unsupported API call.
 */
case class UnexpectedResponse(raw: HttpResponse, lastRequest: Request)
  extends ScarangoError("Wrapper received unexpected HTTP response from ArangoDb")
  with UnprocessedRequest
