package com.auginte.scarango.response

import com.auginte.scarango.response.raw.BoolResponse

/**
 * Response of created resources
 */
trait Created extends ResponseData {
  val name: String
  val raw: BoolResponse
}
