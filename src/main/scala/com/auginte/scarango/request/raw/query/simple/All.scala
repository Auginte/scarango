package com.auginte.scarango.request.raw.query.simple

/**
  * Fetch all documents
  */
case class All(collection: String, skip: Option[Int] = None, limit: Option[Int] = None)
