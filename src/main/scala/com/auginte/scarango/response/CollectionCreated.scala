package com.auginte.scarango.response

case class CollectionCreated(id: String, name: String, waitForSync: Boolean, isVolatile: Boolean, status: Int, `type`: Int, error: Boolean, code: Int) extends Created
