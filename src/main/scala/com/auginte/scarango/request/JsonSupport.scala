package com.auginte.scarango.request

import akka.http.scaladsl.marshallers.sprayjson.SprayJsonSupport
import com.auginte.scarango.common
import spray.json._

trait JsonSupport extends SprayJsonSupport with DefaultJsonProtocol {
  private object CommonJsonSupplier extends common.JsonSupport
  import CommonJsonSupplier._

  implicit val createCollectionFormat = jsonFormat2(raw.create.Collection)
  implicit val createDatabaseUserFormat = jsonFormat4(raw.create.User)
  implicit val createDatabaseFormat = jsonFormat2(raw.create.Database)
  implicit val querySimpleAllFormat = jsonFormat3(raw.query.simple.All)
  implicit val getDocumentFormat = jsonFormat2(raw.get.Document)
  implicit val reokaceDocumentFormat = jsonFormat3(raw.replace.Document)
  implicit val deleteDatabaseFormat = jsonFormat1(raw.delete.Database)
  implicit val deleteDocumentFormat = jsonFormat2(raw.delete.Document)
}