package com.auginte.scarango.request

import spray.http.Uri

case class RemoveCollection(name: String, database: String = "_system") extends RemoveRequest with groups.Collection {
  override lazy val uri: Uri = Uri(s"/_db/$database/_api/collection/$name")
}
