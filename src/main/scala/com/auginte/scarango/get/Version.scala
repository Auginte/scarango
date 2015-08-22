package com.auginte.scarango.get

import spray.http.Uri

/**
 * Request to get version
 */
class Version extends Request {
  override val uri: Uri = Uri("/_api/version")
}

object Version {
  def apply() = new Version
}