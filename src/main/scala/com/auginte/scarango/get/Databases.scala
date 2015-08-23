package com.auginte.scarango.get

import spray.http.Uri

/**
 * List all databases
 */
case object Databases extends Request {
  override val uri: Uri = Uri("/_api/database")
}