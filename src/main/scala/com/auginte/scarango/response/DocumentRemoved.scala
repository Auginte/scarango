package com.auginte.scarango.response

import com.auginte.scarango.response.raw.RawDocumentData

/**
 * Response about document removal
 */
case class DocumentRemoved(database: String, raw: RawDocumentData) extends Data {
  def id = raw._id

  def collection = raw._key
}