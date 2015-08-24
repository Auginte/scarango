package com.auginte.scarango.response.created

case class Collection(id: String, name: String, waitForSync: Boolean, isVolatile: Boolean, status: Int, `type`: Int, error: Boolean, code: Int) extends Created
