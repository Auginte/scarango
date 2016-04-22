package com.auginte.scarango.response.raw.query.simple

import spray.json.JsObject

/**
  * Response for [[com.auginte.scarango.request.raw.query.simple.All]]
  */
case class All(result: List[JsObject], count: Int, error: Boolean, code: Int)
