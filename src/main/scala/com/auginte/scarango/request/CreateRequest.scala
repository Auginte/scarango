package com.auginte.scarango.request

import spray.http._
import spray.json.JsValue

/**
 * Request to create new elements in ArangoDB
 */
abstract class CreateRequest extends Request {
  protected def toJson: JsValue

  protected def entityData: String = toJson.compactPrint

  final val method: HttpMethod = HttpMethods.POST
  final val entity: HttpEntity = HttpEntity(ContentTypes.`application/json`, entityData)
}
