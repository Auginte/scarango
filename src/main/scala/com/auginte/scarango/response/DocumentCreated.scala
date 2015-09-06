package com.auginte.scarango.response

import com.auginte.scarango.response.raw.RawDocumentData

/**
 * Response about document creation
 */
case class DocumentCreated(database: String, raw: RawDocumentData) extends Data {
  def id = raw._id

  def collection = id.split("/")(0)
}