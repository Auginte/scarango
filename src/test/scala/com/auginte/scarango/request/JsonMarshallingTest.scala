package com.auginte.scarango.request

import com.auginte.scarango.common.CollectionTypes
import com.auginte.scarango.request
import com.auginte.scarango.request.raw.create.Collection
import com.auginte.scarango.request.raw.query.simple.All
import org.scalatest.WordSpec
import spray.json._

/**
  * Testing Object->JSON text converting
  */
class JsonMarshallingTest extends WordSpec {
  "CreateCollection class" should {
    "be marshaled with types as numbers" when {
      object RequestJsonProtocol extends request.JsonSupport
      import RequestJsonProtocol._

      "using default parameters" in {
        val defaultValue = Collection("default")
        val json = defaultValue.toJson.compactPrint
        assert(json === """{"name":"default","type":2}""")
      }
      "using edge collection" in {
        val defaultValue = Collection("other", CollectionTypes.Edge)
        val json = defaultValue.toJson.compactPrint
        assert(json === """{"name":"other","type":3}""")
      }
    }
  }
  "Query Simple All class" should {
    "be marshaled with correct limit/skip parameters" when {
      object RequestJsonProtocol extends request.JsonSupport
      import RequestJsonProtocol._

      "they are optional" in {
        val optional = All("some")
        val json = optional.toJson.compactPrint
        assert(json === """{"collection":"some"}""")
      }
      "they are inluded" in {
        val included = All("some", Some(2), Some(10))
        val json = included.toJson.compactPrint
        assert(json === """{"collection":"some","skip":2,"limit":10}""")
      }
    }
  }
}