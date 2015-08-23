package com.auginte.scarango.response.raw

/**
 * Common fields in ArangoDB response
 */
trait RawResponse {
  val error: Boolean
  val code: Int
}
