package com.auginte.scarango.request

import spray.http._

/**
 * Request to get data from ArangoDB without modification.
 */
abstract class GetRequest extends Request {
  final val method: HttpMethod = HttpMethods.GET
  final val entity: HttpEntity = HttpEntity.Empty
}
