package com.auginte.scarango.response.raw

import com.auginte.scarango.response.CommonResponse

case class BoolResponse(result: Boolean, error: Boolean, code: Int) extends CommonResponse
