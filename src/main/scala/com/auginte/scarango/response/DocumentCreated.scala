package com.auginte.scarango.response

import com.auginte.scarango.response.raw.RawDocumentCreated

/**
 * Response about document creation
 */
case class DocumentCreated(id: String, collection: String, raw: RawDocumentCreated) extends Data