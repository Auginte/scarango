package com.auginte.scarango.response.existing

import com.auginte.scarango.response.CommonResponse

case class Databases(result: List[String], error: Boolean, code: Int) extends CommonResponse
