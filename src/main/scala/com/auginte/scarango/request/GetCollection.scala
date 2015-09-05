package com.auginte.scarango.request

import spray.http.Uri

case class GetCollection(name: String) extends GetRequest {
  override val uri: Uri = Uri(s"/_api/collection/$name")
}
