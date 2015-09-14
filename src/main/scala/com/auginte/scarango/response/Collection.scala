package com.auginte.scarango.response

import com.auginte.scarango.response.meta.collection.{EnumStatuses, EnumTypes}
import com.auginte.scarango.response.raw.RawCollection
import com.auginte.scarango.state.DatabaseName

case class Collection(database: DatabaseName, raw: RawCollection)
  extends Data
  with EnumStatuses
  with EnumTypes {

  val id = raw.id
  val name = raw.name
  override val status: Int = raw.status
  override val `type`: Int = raw.`type`
}