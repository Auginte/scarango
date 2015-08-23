package com.auginte.scarango.response.raw

case class BoolResponse(result: Boolean, error: Boolean, code: Int) extends RawResponse
