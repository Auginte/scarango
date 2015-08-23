package com.auginte.scarango.create

import com.auginte.scarango.common
import com.auginte.scarango.common.Authorisation
import spray.http._
import spray.json.JsValue

/**
 * Request to create new elements in ArangoDB
 */
abstract class Request(authorisation: Authorisation = Authorisation.default) extends common.Request(authorisation) {
  protected def toJson: JsValue

  final val method: HttpMethod = HttpMethods.POST
  final val entity: HttpEntity = HttpEntity(ContentTypes.`application/json`, toJson.compactPrint)
}
