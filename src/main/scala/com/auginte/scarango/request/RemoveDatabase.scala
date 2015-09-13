package com.auginte.scarango.request

import com.auginte.scarango.state.DatabaseName
import spray.http.Uri

case class RemoveDatabase(name: DatabaseName) extends RemoveRequest {
  override lazy val uri: Uri = Uri(s"/_api/database/$name")
}
