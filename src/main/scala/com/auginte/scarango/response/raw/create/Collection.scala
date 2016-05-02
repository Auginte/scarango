package com.auginte.scarango.response.raw.create

import com.auginte.scarango.common.{CollectionStatus, CollectionType}

/**
  * Response from [[com.auginte.scarango.request.raw.create.Collection]]
  */
case class Collection(id: String, name: String, waitForSync: Boolean, isVolatile: Boolean, isSystem: Boolean, status: CollectionStatus, `type`: CollectionType, error: Boolean, code: Int)
