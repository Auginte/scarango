package com.auginte.scarango.response

import com.auginte.scarango.state.CollectionName

case class CollectionCreated(id: String, name: CollectionName, waitForSync: Boolean, isVolatile: Boolean, status: Int, `type`: Int, error: Boolean, code: Int) extends Created
