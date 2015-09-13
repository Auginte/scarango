package com.auginte.scarango.request

import com.auginte.scarango.state.DatabaseName
import spray.http.Uri

case class GetCollection(name: String)(implicit val database: DatabaseName = "_system") extends GetRequest with groups.Collection {
  override val uri: Uri = Uri(s"/_db/$database/_api/collection/$name")
}
