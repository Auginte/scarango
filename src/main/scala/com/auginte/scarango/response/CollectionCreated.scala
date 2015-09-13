package com.auginte.scarango.response

import com.auginte.scarango.response.raw.RawCollectionCreated
import com.auginte.scarango.state.DatabaseName

/**
 * Information about newly created collection
 */
case class CollectionCreated(database: DatabaseName, raw: RawCollectionCreated) extends Created {
  val name = raw.name
  def id = raw.id
}
