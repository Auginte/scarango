package com.auginte.scarango.request

import spray.http.Uri

case class RemoveCollection(name: String) extends RemoveRequest {
  override lazy val uri: Uri = Uri(s"/_api/collection/$name")
}
