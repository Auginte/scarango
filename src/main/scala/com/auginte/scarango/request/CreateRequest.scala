package com.auginte.scarango.request

import com.auginte.scarango.common.Authorisation
import spray.http._
import spray.json.JsValue

/**
 * Request to create new elements in ArangoDB
 */
abstract class CreateRequest(authorisation: Authorisation = Authorisation.default) extends Request(authorisation) {
  protected def toJson: JsValue

  final val method: HttpMethod = HttpMethods.POST
  final val entity: HttpEntity = HttpEntity(ContentTypes.`application/json`, toJson.compactPrint)
}
