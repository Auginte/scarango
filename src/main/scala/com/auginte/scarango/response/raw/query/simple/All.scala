package com.auginte.scarango.response.raw.query.simple

/**
  * Response for [[com.auginte.scarango.request.raw.query.simple.All]]
  */
case class All(result: List[Document], count: Int, error: Boolean, code: Int)
