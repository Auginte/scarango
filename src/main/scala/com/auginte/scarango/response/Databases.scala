package com.auginte.scarango.response


case class Databases(result: List[String], error: Boolean, code: Int) extends StandardResponse
