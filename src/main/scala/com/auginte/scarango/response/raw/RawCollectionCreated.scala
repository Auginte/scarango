package com.auginte.scarango.response.raw

import com.auginte.scarango.response.Created
import com.auginte.scarango.state.CollectionName

case class RawCollectionCreated(id: String, name: CollectionName, waitForSync: Boolean, isVolatile: Boolean, status: Int, `type`: Int, error: Boolean, code: Int)
