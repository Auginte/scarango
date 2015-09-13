package com.auginte.scarango.response

import com.auginte.scarango.response.raw.RawDocumentData
import com.auginte.scarango.state.{DatabaseName, CollectionName}

/**
 * Response about document creation
 */
case class DocumentCreated(database: DatabaseName, raw: RawDocumentData) extends Data {
  def id = raw._id

  def collection = CollectionName(id.split("/")(0))
}