package com.auginte.scarango.response

/**
 * Common fields in ArangoDB REST API response
 */
trait CommonResponse extends ResponseData {
  val error: Boolean
  val code: Int
}
