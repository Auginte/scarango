package com.auginte.scarango.request.raw.create

import spray.json.JsObject

/**
  * User data in Database query
  *
  * @see [[com.auginte.scarango.request.raw.create.Database]]
  */
case class User(username: String, passwd: String, active: Boolean = true, extra: JsObject = JsObject.empty)
