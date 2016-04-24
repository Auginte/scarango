package com.auginte.scarango.request

import akka.http.scaladsl.marshallers.sprayjson.SprayJsonSupport
import com.auginte.scarango.common
import spray.json._

trait JsonSupport extends SprayJsonSupport with DefaultJsonProtocol {
  private object CommonJsonSupplier extends common.JsonSupport
  import CommonJsonSupplier._

  implicit val createCollectionFormat = jsonFormat2(raw.create.Collection)
  implicit val createDatabaseFormat = jsonFormat1(raw.create.Database)
  implicit val querySimpleAllFormat = jsonFormat3(raw.query.simple.All)
  implicit val deleteDatabaseFormat = jsonFormat1(raw.delete.Database)
}