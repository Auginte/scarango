package com.auginte.scarango.errors

import com.auginte.scarango.get.Request

case class UnexpectedRequest(raw: Any, lastRequest: Request)
  extends ScarangoError("Request not supported by wrapper. Passing object instead of instance?")
  with UnprocessedRequest
