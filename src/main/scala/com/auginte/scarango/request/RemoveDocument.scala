package com.auginte.scarango.request

import com.auginte.scarango.state.DatabaseName
import spray.http.Uri

case class RemoveDocument(id: String)(implicit val database: DatabaseName = "_system") extends RemoveRequest with groups.Document {
  override lazy val uri: Uri = Uri(s"/_db/$database/_api/document/$id")
}
