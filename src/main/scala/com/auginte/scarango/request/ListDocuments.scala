package com.auginte.scarango.request

import spray.http.Uri

case class ListDocuments(collection: String, database: String = "_system") extends GetRequest with groups.Document {
  override val uri: Uri = Uri(s"/_db/$database/_api/document/?collection=$collection")
}
