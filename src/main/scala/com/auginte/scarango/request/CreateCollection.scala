package com.auginte.scarango.request

import com.auginte.scarango.macros.SelfJson
import com.auginte.scarango.state.{DatabaseNames, CollectionName, DatabaseName}
import spray.http.Uri
import spray.json._
import spray.json.DefaultJsonProtocol._

case class CreateCollection(name: CollectionName)(implicit val database: DatabaseName = DatabaseNames.default) extends CreateRequest with groups.Collection {
  override def toJson = SelfJson.materialise[this.type]

  override val uri: Uri = Uri(s"/_db/$database/_api/collection")
}
