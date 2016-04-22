package com.auginte.scarango

import akka.stream.scaladsl.{Sink, Source}
import com.auginte.scarango.common.{CollectionStatuses, CollectionTypes}
import com.auginte.scarango.helpers.AkkaSpec
import com.auginte.scarango.request.raw.create.{Collection, Document}
import com.auginte.scarango.request.raw.query.simple.All

/**
  * Testing integration with ArangoDB
  */
class IntegrationTest extends AkkaSpec {
  "To be easy to use Scarango driver in various abstraction levels" should {
    "have one-liner for final result (blocking is acceptable)" in withDriver { scarango =>
      assert(scarango.Results.version().version === latestApiVersion)
    }
    "have future for common situations" in withDriver { scarango =>
      withDelay {
        scarango.Futures.version()
      } { raw =>
        assert(raw.version === latestApiVersion)
        assert(raw.server === "arango")
      }
    }
    "have flow to complete it by self" in {
      val context = defaultConfig
      implicit val system = context.actorSystem
      implicit val materializer = context.materializer
      val scarango = new Scarango(context)
      withDelay {
        scarango.Flows.version.runWith(Sink.head).flatMap(same => same)
      } { raw =>
        assert(raw.version === latestApiVersion)
        assert(raw.server === "arango")
      }
    }
    "have access to all parts to create flow from scratch" in {
      implicit val context = defaultConfig
      implicit val system = context.actorSystem
      implicit val materializer = context.materializer
      withDelay {
        Source.single(request.getVersion)
          .via(state.database)
          .map (response.toVersion)
          .runWith(Sink.head)
          .flatMap(same => same)
      } { raw =>
        assert(raw.version === latestApiVersion)
        assert(raw.server === "arango")
      }
    }
    "can receive RAW JSON response from ArangoDb" in {
      implicit val context = defaultConfig
      implicit val system = context.actorSystem
      implicit val materializer = context.materializer
      withDelay {
        Source.single(request.getVersion)
          .via(state.database)
          .map (response.toRaw)
          .runWith(Sink.head)
          .flatMap(same => same)
      } { raw =>
        assert(raw === s"""{"server":"arango","version":"$latestApiVersion"}""")
      }
    }
  }

  "To cover ArangoDB API" should {
    "get version of ArangoDB" in withDriver { scarango =>
      withDelay {
        scarango.Futures.version()
      } { raw =>
        assert(raw.version === latestApiVersion)
        assert(raw.server === "arango")
      }
    }
    "create new collection" in withDriver { scarango =>
      val name = "collection" + randomId
      val response = scarango.Results.create(Collection(name))
      assert(response.name === name)
      assert(response.`type` === CollectionTypes.Document)
      assert(response.error === false)
      assert(response.code === 200)
      assert(response.status === CollectionStatuses.Loaded)
    }
    "create new document and can fetch them" in withDriver { scarango =>
      val collectionName = "with-data" + randomId
      scarango.Results.create(Collection(collectionName))
      for (i <- 1 to 5) {
        val rawData = s"""{"Hello": $i}"""
        val response = scarango.Results.create(Document(rawData, collectionName))
        assert(response.error === false)
        assert(response._id === collectionName + "/" + response._key)
      }
      val response = scarango.Results.query(All(collectionName))
      assert(response.result.size === 5)
      for (document <- response.result) {
        val id = document.fields("_id").toString().replace("\"", "")
        val key = document.fields("_key").toString().replace("\"", "")
        assert(id === collectionName + "/" + key)
      }
    }
  }
}