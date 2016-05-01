package com.auginte.scarango.request.raw.get

/**
  * Domain object for GET /_api/document/{collection-name}/{document-key}
  */
case class Document(collectionName: String, key: String)
