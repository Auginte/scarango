package com.auginte.scarango.request

import com.auginte.scarango.common.Authorisation
import spray.http._

/**
 * Request to get data from ArangoDB without modification.
 */
abstract class GetRequest(authorisation: Authorisation = Authorisation.default) extends Request(authorisation) {
  final val method: HttpMethod = HttpMethods.GET
  final val entity: HttpEntity = HttpEntity.Empty
}
