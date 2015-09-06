package com.auginte.scarango.request

import spray.http.Uri

/**
 * List all databases
 */
case object ListDatabases extends GetRequest with groups.Database {
  override val uri: Uri = Uri("/_api/database")

  override def toString: String = "Get.Databases"
}