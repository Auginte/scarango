package com.auginte.scarango.request

import com.auginte.scarango.request.parts.User
import com.auginte.scarango.state.DatabaseName
import spray.http.Uri
import spray.json.{JsValue, JsArray, JsObject, JsString}

import scala.language.implicitConversions

case class CreateDatabase(name: DatabaseName, users: List[User] = List()) extends CreateRequest with groups.Database{
  override def toJson = new JsObject(withUsersJson(mainJsonFields))

  private def mainJsonFields: Map[String, JsValue] = Map("name" -> JsString(name.name))

  private def withUsersJson(map: Map[String, JsValue]) =
    if (users.nonEmpty) map.updated("users", JsArray(users.map(_.toJson).toVector))
    else map

  override val uri: Uri = Uri("/_api/database")
}
