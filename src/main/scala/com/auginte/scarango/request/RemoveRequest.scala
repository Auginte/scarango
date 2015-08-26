package com.auginte.scarango.request

import spray.http._

/**
 * Request to remove elements from ArangoDB
 */
abstract class RemoveRequest extends Request {
  final val method: HttpMethod = HttpMethods.DELETE
  val entity: HttpEntity = HttpEntity.Empty
}
