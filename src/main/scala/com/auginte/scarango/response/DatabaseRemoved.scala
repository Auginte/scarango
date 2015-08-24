package com.auginte.scarango.response

import com.auginte.scarango.request.RemoveDatabase
import com.auginte.scarango.response.raw.BoolResponse

/**
 * See [[com.auginte.scarango.request.RemoveDatabase]]
 */
case class DatabaseRemoved(element: RemoveDatabase, raw: BoolResponse) extends Removed
