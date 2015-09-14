package com.auginte.scarango.request.parts

import spray.json._

import scala.language.implicitConversions

/**
 * Database user
 */
case class User(name: String, password: String, active: Boolean = true, extras: Map[String, String] = Map()) {
  def toJson: JsValue = JsObject(withExtrasJson(mainFieldsJson))

  private def mainFieldsJson: Map[String, JsValue] = Map(
    "username" -> JsString(name),
    "passwd" -> JsString(password),
    "active" -> JsBoolean(active)
  )

  private def withExtrasJson(oldFields: Map[String, JsValue]): Map[String, JsValue] =
    if (extras.nonEmpty) oldFields.updated("extra", JsObject(extras.map(pair => pair._1 -> JsString(pair._2))))
    else oldFields

  implicit def userList2Json(l: List[User]): JsonWriter[List[User]] = new JsonWriter[List[User]] {
    override def write(obj: List[User]): JsValue = JsArray(obj.map(_.toJson).toVector)
  }
}