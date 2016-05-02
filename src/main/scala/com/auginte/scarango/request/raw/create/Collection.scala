package com.auginte.scarango.request.raw.create

import com.auginte.scarango.common.{CollectionType, CollectionTypes}

/**
  * Create collection.
  * POST:  /_api/collection
  */
case class Collection(name: String, `type`: CollectionType = CollectionTypes.Document)