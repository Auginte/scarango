package com.auginte.scarango.request

import spray.http.Uri

case class GetCollection(name: String) extends GetRequest with groups.Collection {
  override val uri: Uri = Uri(s"/_api/collection/$name")
}
