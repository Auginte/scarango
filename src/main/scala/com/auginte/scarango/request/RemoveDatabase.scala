package com.auginte.scarango.request

import spray.http.Uri

case class RemoveDatabase(name: String) extends RemoveRequest {
  override lazy val uri: Uri = Uri(s"/_api/database/$name")
}
