package com.auginte.scarango.response.raw.list

import com.auginte.scarango.common.{CollectionStatus, CollectionType}

/**
  * Collection data structure inside: GET /_api/collection
  */
case class Collection(id: String, name: String, isSystem: Boolean, status: CollectionStatus, `type`: CollectionType)
