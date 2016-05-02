package com.auginte.scarango.response.raw.create

import com.auginte.scarango.common.{CollectionStatus, CollectionType}

/**
  * Response from [[com.auginte.scarango.request.raw.create.Database]]
  */
case class Database(result: Boolean, error: Boolean, code: Int)
