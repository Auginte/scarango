package com.auginte.scarango.response

import akka.http.scaladsl.marshallers.sprayjson.SprayJsonSupport
import com.auginte.scarango.common
import com.auginte.scarango.common.{CollectionStatus, CollectionType, CollectionTypes}
import com.auginte.scarango.response.raw.query.simple.Document
import spray.json._

trait JsonSupport extends SprayJsonSupport with DefaultJsonProtocol {
  private object CommonJsonSupport extends common.JsonSupport
  import CommonJsonSupport._

  implicit object DocumentFormat extends RootJsonFormat[Document] {
    def write(c: Document) = new JsObject(c.fields)

    def read(value: JsValue) = value match {
      case o: JsObject if o.fields.contains("_key") => new Document(o.fields)
      case _ => deserializationError("Document should be object with mandatory fields: _key")
    }
  }

  implicit val versionFormat = jsonFormat2(raw.Version)
  implicit val createDatabaseFormat = jsonFormat3(raw.create.Database)
  implicit val createCollectionFormat = jsonFormat9(raw.create.Collection)
  implicit val createDocumentFormat = jsonFormat4(raw.create.Document)
  implicit val querySimpleAllFormat = jsonFormat4(raw.query.simple.All)
  implicit val listDatabasesFormat = jsonFormat3(raw.list.Databases)
  implicit val listCollectionFormat = jsonFormat5(raw.list.Collection)
  implicit val listCollectionsFormat = jsonFormat3(raw.list.Collections)
  implicit val deleteDatabasesFormat = jsonFormat3(raw.delete.Database)
}