package com.auginte.scarango.response

import com.auginte.scarango.response.raw.BoolResponse
import com.auginte.scarango.state.DatabaseName

/**
 * Response from [[com.auginte.scarango.request.CreateDatabase]]
 */
case class DatabaseCreated(name: DatabaseName, raw: BoolResponse) extends Created