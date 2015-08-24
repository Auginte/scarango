package com.auginte.scarango.response.removed

import com.auginte.scarango.remove
import com.auginte.scarango.response.raw.IdResponse

/**
 * Wrapper about removed ArangoDB collection
 */
case class Collection(element: remove.Collection, raw: IdResponse) extends Removed
