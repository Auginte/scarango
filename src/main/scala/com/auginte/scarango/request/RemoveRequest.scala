package com.auginte.scarango.request

import com.auginte.scarango.common.Authorisation
import spray.http._

/**
 * Request to remove elements from ArangoDB
 */
abstract class RemoveRequest(authorisation: Authorisation = Authorisation.default) extends Request(authorisation) {
  final val method: HttpMethod = HttpMethods.DELETE
  val entity: HttpEntity = HttpEntity.Empty
}
