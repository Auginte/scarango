package com.auginte.scarango.request.raw.delete

/**
  * Domain object for: DELETE /_api/document/{collection-name}/{document-key}
  */
case class Document(collectionName: String, key: String)
