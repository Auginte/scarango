package com.auginte.scarango.response.created

import com.auginte.scarango.response.ResponseData

/**
 * Response of created resources
 */
trait Created extends ResponseData {
  val name: String
}
