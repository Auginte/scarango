package com.auginte.scarango.request

import spray.http.Uri
import spray.json._

case class CreateDatabase(name: String) extends CreateRequest {
  override def toJson: JsValue = JsObject(
    "name" -> JsString(name)
  )

  override val uri: Uri = Uri("/_api/database")
}
