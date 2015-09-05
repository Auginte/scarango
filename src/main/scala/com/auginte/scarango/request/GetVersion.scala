package com.auginte.scarango.request

import spray.http.Uri

/**
 * Request to get version
 */
case object GetVersion extends GetRequest with groups.General {
  override val uri: Uri = Uri("/_api/version")
}