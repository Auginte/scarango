package com.auginte.scarango.response

import com.auginte.scarango.response.raw.BoolResponse

/**
 * Response from [[com.auginte.scarango.request.CreateDatabase]]
 */
case class DatabaseCreated(name: String, raw: BoolResponse) extends Created