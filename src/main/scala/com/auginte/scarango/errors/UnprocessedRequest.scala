package com.auginte.scarango.errors

import com.auginte.scarango.get.Request

trait UnprocessedRequest {
  val lastRequest: Request
}