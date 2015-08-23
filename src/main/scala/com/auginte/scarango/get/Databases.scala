package com.auginte.scarango.get

import spray.http.Uri

/**
 * List all databases
 */
class Databases extends Request {
  override val uri: Uri = Uri("/_api/database")
}

object Databases {
  def apply() = new Databases
}