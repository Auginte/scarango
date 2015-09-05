package com.auginte.scarango.request

import com.auginte.scarango.macros.SelfJson
import spray.http.Uri
import spray.json._
import spray.json.DefaultJsonProtocol._

case class CreateCollection(name: String) extends CreateRequest with groups.Collection {
  override def toJson = SelfJson.materialise[this.type]

  override val uri: Uri = Uri("/_api/collection")
}
