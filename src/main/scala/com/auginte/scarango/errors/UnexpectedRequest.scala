package com.auginte.scarango.errors

/**
 * Error not directly related to ArangoDB REST api response
 *
 * Common error is to use:
 * {{{
 *   db ! get.Version
 * }}}
 * instead of
 * {{{
 *   db ! get.Version()
 * }}}
 *
 * See `raw` for context
 */
case class UnexpectedRequest(raw: Any)
  extends ScarangoError("unexpected state. Passing object instead of instance?")