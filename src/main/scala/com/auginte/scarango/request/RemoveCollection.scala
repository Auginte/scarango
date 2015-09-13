package com.auginte.scarango.request

import com.auginte.scarango.state.DatabaseName
import spray.http.Uri

case class RemoveCollection(name: String)(implicit val database: DatabaseName = "_system") extends RemoveRequest with groups.Collection {
  override lazy val uri: Uri = Uri(s"/_db/$database/_api/collection/$name")
}
