package com.auginte.scarango.response.raw.delete

/**
  * Response of [[com.auginte.scarango.request.raw.delete.Document]]
  */
case class Document(_id: String, _rev: String, _key: String, error: Boolean)
