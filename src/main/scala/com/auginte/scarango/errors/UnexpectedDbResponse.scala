package com.auginte.scarango.errors

import com.auginte.scarango.get.Request

case class UnexpectedDbResponse(raw: Any, lastRequest: Request)
  extends ScarangoError("Wrapper received unexpected HTTP response from ArangoDb")
  with UnprocessedRequest
