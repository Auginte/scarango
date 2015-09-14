package com.auginte.scarango.common

import com.auginte.scarango.request.parts.Authorisation
import com.auginte.scarango.state.DatabaseName

/**
 * Properties to simulate state between request and response
 */
trait StateParameters {
  val id: Any
  val authorisation: Authorisation
  val database: DatabaseName
}