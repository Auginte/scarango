package com.auginte.scarango.get

import spray.http.Uri

/**
 * Request to get version
 */
case object Version extends Request {
  override val uri: Uri = Uri("/_api/version")
}