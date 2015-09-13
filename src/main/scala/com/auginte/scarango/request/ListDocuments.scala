package com.auginte.scarango.request

import com.auginte.scarango.state.DatabaseName
import spray.http.Uri

case class ListDocuments(collection: String)(implicit val database: DatabaseName = "_system") extends GetRequest with groups.Document {
  override val uri: Uri = Uri(s"/_db/$database/_api/document/?collection=$collection")
}
