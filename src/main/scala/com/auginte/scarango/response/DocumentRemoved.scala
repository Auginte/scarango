package com.auginte.scarango.response

import com.auginte.scarango.response.raw.RawDocumentData
import com.auginte.scarango.state.DatabaseName

/**
 * Response about document removal
 */
case class DocumentRemoved(database: DatabaseName, raw: RawDocumentData) extends Data {
  def id = raw._id

  def collection = raw._key
}