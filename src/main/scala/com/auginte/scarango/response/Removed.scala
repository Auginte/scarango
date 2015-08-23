package com.auginte.scarango.response

import com.auginte.scarango.remove
import com.auginte.scarango.response.raw.BoolResponse

/**
 * Wrapper about removed ArangoDB element
 */
case class Removed(element: remove.Request, raw: BoolResponse) extends ResponseData
