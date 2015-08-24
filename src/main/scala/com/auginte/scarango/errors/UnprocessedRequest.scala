package com.auginte.scarango.errors

import com.auginte.scarango.request.Request

/**
 * Marker to identify errors, that need user/client to take action to recover last failed request.
 */
trait UnprocessedRequest extends ScarangoError {
  val lastRequest: Request
}