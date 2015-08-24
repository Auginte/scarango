package com.auginte.scarango.response.raw

import com.auginte.scarango.response.common.CommonResponse

case class IdResponse(id: String, error: Boolean, code: Int) extends CommonResponse
