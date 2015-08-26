package com.auginte.scarango.common

/**
 * Properties to simulate state between request and response
 */
trait StateParameters {
  val id: Any
  val authorisation: Authorisation
  val database: String
}