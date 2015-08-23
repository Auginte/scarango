package com.auginte.scarango.get

import com.auginte.scarango.common
import com.auginte.scarango.common.Authorisation
import spray.http._

/**
 * Request to get data from ArangoDB without modification.
 */
abstract class Request(authorisation: Authorisation = Authorisation.default) extends common.Request(authorisation) {
  final val method: HttpMethod = HttpMethods.GET
  final val entity: HttpEntity = HttpEntity.Empty
}
