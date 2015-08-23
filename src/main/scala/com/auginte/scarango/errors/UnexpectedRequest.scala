package com.auginte.scarango.errors

/**
 * Error not directly related to ArangoDB REST api response
 */
case class UnexpectedRequest(raw: Any)
  extends ScarangoError("Unexpected state. Passing not Request object/instance?")