package com.auginte.scarango.request

import spray.http.Uri

case class RemoveDocument(id: String, database: String = "_system") extends RemoveRequest with groups.Document {
  override lazy val uri: Uri = Uri(s"/_db/$database/_api/document/$id")
}
