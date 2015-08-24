package com.auginte.scarango.response

import com.auginte.scarango.request.RemoveCollection
import com.auginte.scarango.response.raw.IdResponse

/**
 * Wrapper about removed ArangoDB collection
 */
case class CollectionRemoved(element: RemoveCollection, raw: IdResponse) extends Removed
