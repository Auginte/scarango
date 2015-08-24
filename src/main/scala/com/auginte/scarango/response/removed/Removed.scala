package com.auginte.scarango.response.removed

import com.auginte.scarango.remove
import com.auginte.scarango.response.{CommonResponse, ResponseData}

/**
 * Common structure for responses about removed structures
 */
trait Removed extends ResponseData {
  val element: remove.Request
  val raw: CommonResponse
}
