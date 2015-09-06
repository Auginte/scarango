package com.auginte.scarango.request

import spray.http.Uri

case class GetCollection(name: String, database: String = "_system") extends GetRequest with groups.Collection {
  override val uri: Uri = Uri(s"/_db/$database/_api/collection/$name")
}
