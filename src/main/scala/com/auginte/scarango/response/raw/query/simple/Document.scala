package com.auginte.scarango.response.raw.query.simple

import spray.json.{JsObject, JsString, JsValue}

/**
  * Model for raw response of Document
  */
class Document(fields: Map[String, JsValue]) extends JsObject(fields) {
  def id = fieldToString("_id")
  def revision = fieldToString("_rev")
  def key = fieldToString("_key")

  private def jsToString(value: JsValue): String = value match {
    case JsString(s) => s
    case other => other.toString
  }

  private def fieldToString(key: String) = jsToString(fields(key))
}
