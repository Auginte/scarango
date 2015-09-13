package com.auginte.scarango.request

import com.auginte.scarango.macros.SelfJson
import com.auginte.scarango.state.DatabaseName
import spray.http.Uri
import spray.json._
import spray.json.DefaultJsonProtocol._

case class CreateDatabase(name: DatabaseName) extends CreateRequest with groups.Database{
  override def toJson = SelfJson.materialise[this.type]

  override val uri: Uri = Uri("/_api/database")
}
