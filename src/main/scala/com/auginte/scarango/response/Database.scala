package com.auginte.scarango.response

import com.auginte.scarango.response.raw.BoolResponse

/**
 * Response from [[com.auginte.scarango.create.Database]]
 */
case class Database(name: String, raw: BoolResponse) extends Created