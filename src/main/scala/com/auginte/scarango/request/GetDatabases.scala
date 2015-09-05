package com.auginte.scarango.request

import spray.http.Uri

/**
 * List all databases
 */
case object GetDatabases extends GetRequest with groups.Database {
  override val uri: Uri = Uri("/_api/database")

  override def toString: String = "Get.Databases"
}