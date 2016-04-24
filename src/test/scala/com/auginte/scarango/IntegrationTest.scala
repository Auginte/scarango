package com.auginte.scarango

import akka.stream.scaladsl.{Sink, Source}
import com.auginte.scarango.common.{CollectionStatuses, CollectionTypes}
import com.auginte.scarango.helpers.AkkaSpec
import com.auginte.scarango.request.raw.{create => c}
import com.auginte.scarango.request.raw.{delete => d}
import com.auginte.scarango.request.raw.query.simple.All

import scala.concurrent.Await
import scala.concurrent.duration._

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
          .map(response.toVersion)
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
          .map(response.toRaw)
          .runWith(Sink.head)
          .flatMap(same => same)
      } { raw =>
        assert(raw === s"""{"server":"arango","version":"$latestApiVersion"}""")
      }
    }
  }

  "To interact with database like a streams" should {
    "retrieve records in a (simulated) asynchronous way" in withDriver { scarango =>
      val collectionName = "with-data" + randomId
      implicit val context = scarango.context
      implicit val materializer = context.materializer
      scarango.Results.create(c.Collection(collectionName))
      for (i <- 1 to 20) {
        val rawData = s"""{"Hello": $i}"""
        scarango.Results.create(c.Document(rawData, collectionName))
      }

      val getAll = All(collectionName)
      val graph = scarango.Flows.query(getAll)
        .map(_.map(_.result))
        .flatMapConcat(Source.fromFuture)
        .expand(_.iterator)
      val resultViaIterator = Await.result(graph.map(_.prettyPrint).runReduce(_ + _), 4.seconds)
      val resultOfWhole = scarango.Results.query(getAll).result.map(_.prettyPrint).mkString("")
      val resultIterator = scarango.Results.iterator(getAll).map(_.prettyPrint).toList.mkString("")
      assert(resultViaIterator === resultOfWhole)
      assert(resultViaIterator === resultIterator)
    }
  }

  "To cover ArangoDB API" when {
    "testing administration part" should {
      "get version of ArangoDB" in withDriver { scarango =>
        withDelay {
          scarango.Futures.version()
        } { raw =>
          assert(raw.version === latestApiVersion)
          assert(raw.server === "arango")
        }
      }
    }
    "testing database" should withDriver { scarango =>
      val name = "db" + randomId
      "be able to create new database" in {
        info(s"Database: $name")
        val response = scarango.Results.create(c.Database(name))
        assert(response.result === true)
        assert(response.error === false)
        assert(response.code === HttpStatusCodes.created)
      }
      "be able to list existing databases" in {
        val databases = scarango.Results.listDatabases()
        assert(databases.code === HttpStatusCodes.ok)
        assert(databases.error === false)
        assert(databases.result.contains(name))
        info(s"Databases: ${databases.result}")
      }
      "be able to remove database" in {
        val response = scarango.Results.delete(d.Database(name))
        assert(response.result === true)
        assert(response.error === false)
        assert(response.code === HttpStatusCodes.ok)
        val databases = scarango.Results.listDatabases()
        assert(databases.result.contains(name) === false)
        info(s"Databases: ${databases.result}")
      }
    }
    "testing collections" should {
      "be able to create new collection" in withDriver { scarango =>
        val name = "collection" + randomId
        info(s"Database: _system. Collection: $name")
        val response = scarango.Results.create(c.Collection(name))
        assert(response.name === name)
        assert(response.`type` === CollectionTypes.Document)
        assert(response.error === false)
        assert(response.code === HttpStatusCodes.ok)
        assert(response.status === CollectionStatuses.Loaded)
      }
      "be able to create new collection in custom database" in withDriver { scarango =>
        val databaseName = "db" + randomId
        val collectionName = "inside" + randomId
        info(s"Database: $databaseName. Collection: $collectionName")
        val dbCreated = scarango.Results.create(c.Database(databaseName))
        assert(dbCreated.result === true)
        val inCreatedDb = scarango.withDatabase(databaseName)
        val collectionCreated = inCreatedDb.Results.create(c.Collection(collectionName))
        assert(collectionCreated.error === false)
        val dbRemoved = scarango.Results.delete(d.Database(databaseName))
        assert(dbRemoved.result === true)
      }
    }
    "testing documents" should withDriver { scarango =>
      val collectionName = "with-data" + randomId
      "be able to create new documents" in {
        info(s"Database: _system. Collection: $collectionName")
        scarango.Results.create(c.Collection(collectionName))
        for (i <- 1 to 5) {
          val rawData = s"""{"Hello": $i}"""
          val response = scarango.Results.create(c.Document(rawData, collectionName))
          assert(response.error === false)
          assert(response._id === collectionName + "/" + response._key)
        }
      }
      "be able to list created documents" in {
        val response = scarango.Results.query(All(collectionName))
        info(s"Database: _system. Collection: $collectionName Documents: ${response.result.map(_.id)}")
        assert(response.result.size === 5)
        for (document <- response.result) {
          val id = document.id
          val key = document.key
          assert(id === collectionName + "/" + key)
        }
      }
    }
  }
}