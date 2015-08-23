package com.auginte.scarango.response

/**
 * Common fields included in successful ArangoDB response
 */
trait StandardResponse extends ResponseData {
  val error: Boolean
  val code: Int
}
