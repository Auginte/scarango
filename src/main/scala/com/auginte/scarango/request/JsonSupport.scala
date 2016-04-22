package com.auginte.scarango.request

import akka.http.scaladsl.marshallers.sprayjson.SprayJsonSupport
import com.auginte.scarango.common
import com.auginte.scarango.request.raw.create.Collection
import spray.json._

trait JsonSupport extends SprayJsonSupport with DefaultJsonProtocol {
  private object CommonJsonSuppoer extends common.JsonSupport
  import CommonJsonSuppoer._

  implicit val createCollectionFormat = jsonFormat2(Collection)
}