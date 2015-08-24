package com.auginte.scarango.response

import com.auginte.scarango.request.RemoveRequest
import com.auginte.scarango.response.common.CommonResponse

/**
 * Common structure for responses about removed structures
 */
trait Removed extends Response {
  val element: RemoveRequest
  val raw: CommonResponse
}
