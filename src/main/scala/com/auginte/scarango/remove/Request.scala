package com.auginte.scarango.remove

import com.auginte.scarango.common
import com.auginte.scarango.common.Authorisation
import spray.http._

/**
 * Request to remove elements from ArangoDB
 */
abstract class Request(authorisation: Authorisation = Authorisation.default) extends common.Request(authorisation) {
  final val method: HttpMethod = HttpMethods.DELETE
  val entity: HttpEntity = HttpEntity.Empty
}
