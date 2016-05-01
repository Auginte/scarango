package com.auginte.scarango.request.raw.replace

/**
  * Replace content of the document
  *
  * PUT /_api/document/{collection-name}/{document-key}
  */
case class Document(rawData: String, collectionName: String, key: String)
