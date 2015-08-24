package com.auginte.scarango.response.removed

import com.auginte.scarango.remove
import com.auginte.scarango.response.raw.BoolResponse

/**
 * Wrapper about removed ArangoDB database
 */
case class Database(element: remove.Database, raw: BoolResponse) extends Removed
