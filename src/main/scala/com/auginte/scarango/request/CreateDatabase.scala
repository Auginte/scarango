package com.auginte.scarango.request

import com.auginte.scarango.macros.SelfJson
import spray.http.Uri
import spray.json._
import spray.json.DefaultJsonProtocol._

case class CreateDatabase(name: String) extends CreateRequest {
  override def toJson = SelfJson.materialise[this.type ]

  override val uri: Uri = Uri("/_api/database")
}
