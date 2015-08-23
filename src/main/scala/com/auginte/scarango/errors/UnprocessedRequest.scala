package com.auginte.scarango.errors

import com.auginte.scarango.common.Request

trait UnprocessedRequest {
  val lastRequest: Request
}