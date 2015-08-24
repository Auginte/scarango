package com.auginte.scarango.response.common

import com.auginte.scarango.response.Response

/**
 * Common fields in ArangoDB REST API response
 */
trait CommonResponse extends Response {
  val error: Boolean
  val code: Int
}
