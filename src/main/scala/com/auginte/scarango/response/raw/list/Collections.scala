package com.auginte.scarango.response.raw.list

/**
  * Response from: GET /_api/collection
  */
case class Collections(collections: List[Collection], error: Boolean, code: Int)
