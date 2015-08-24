package com.auginte.scarango.response

import com.auginte.scarango.request.CreateDatabase
import com.auginte.scarango.response.raw.BoolResponse

/**
 * Response from [[CreateDatabase]]
 */
case class DatabaseCreated(name: String, raw: BoolResponse) extends Created