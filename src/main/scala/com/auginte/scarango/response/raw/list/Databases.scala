package com.auginte.scarango.response.raw.list

/**
  * Response from: GET /_api/database
  */
case class Databases(result: List[String], error: Boolean, code: Int)
