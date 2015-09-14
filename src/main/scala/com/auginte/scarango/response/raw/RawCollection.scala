package com.auginte.scarango.response.raw

import com.auginte.scarango.state.CollectionName

case class RawCollection(id: String, name: CollectionName, isSystem: Boolean, status: Int, `type`: Int, error: Boolean, code: Int)