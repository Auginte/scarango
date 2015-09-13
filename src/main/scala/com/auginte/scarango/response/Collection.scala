package com.auginte.scarango.response

import com.auginte.scarango.response.common.CommonResponse
import com.auginte.scarango.response.meta.collection.{EnumStatuses, EnumTypes}
import com.auginte.scarango.state.CollectionName

case class Collection(id: String, name: CollectionName, isSystem: Boolean, status: Int, `type`: Int, error: Boolean, code: Int)
  extends Data
  with CommonResponse
  with EnumStatuses
  with EnumTypes