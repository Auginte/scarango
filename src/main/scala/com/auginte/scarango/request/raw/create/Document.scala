package com.auginte.scarango.request.raw.create

import com.auginte.scarango.common.{CollectionType, CollectionTypes}

/**
  * Create Document.
  * POST:  /_api/document
  */
case class Document(rawData: String, collectionName: String)