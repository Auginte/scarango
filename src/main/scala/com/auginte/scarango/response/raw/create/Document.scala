package com.auginte.scarango.response.raw.create

import com.auginte.scarango.common.{CollectionStatus, CollectionType}

/**
  * Response from [[com.auginte.scarango.request.raw.create.Document]]
  */
case class Document(_id: String, _rev: String, _key: String, error: Boolean)