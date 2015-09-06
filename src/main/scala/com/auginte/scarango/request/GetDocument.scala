package com.auginte.scarango.request

import spray.http.Uri

case class GetDocument(id: String, database: String = "_system") extends GetRequest with groups.Document {
  override val uri: Uri = Uri(s"/_db/$database/_api/document/$id")
}
