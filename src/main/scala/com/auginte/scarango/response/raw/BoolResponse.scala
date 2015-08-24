package com.auginte.scarango.response.raw

import com.auginte.scarango.response.common.CommonResponse

case class BoolResponse(result: Boolean, error: Boolean, code: Int) extends CommonResponse
