package com.auginte.scarango.request

import akka.http.scaladsl.marshallers.sprayjson.SprayJsonSupport
import com.auginte.scarango.common
import com.auginte.scarango.request.raw.create.{Collection, Document}
import com.auginte.scarango.request.raw.query.simple.All
import spray.json._

trait JsonSupport extends SprayJsonSupport with DefaultJsonProtocol {
  private object CommonJsonSupplier extends common.JsonSupport
  import CommonJsonSupplier._

  implicit val createCollectionFormat = jsonFormat2(Collection)
  implicit val querySimpleAllFormat = jsonFormat3(All)
}