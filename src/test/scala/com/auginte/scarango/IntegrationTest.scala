package com.auginte.scarango

import akka.stream.scaladsl.{Sink, Source}
import com.auginte.scarango.common.{CollectionStatuses, CollectionTypes}
import com.auginte.scarango.helpers.AkkaSpec
import com.auginte.scarango.request.raw.query.simple.All
import com.auginte.scarango.request.raw.{create => c, delete => d, get => g}
import com.auginte.scarango.response.raw.query.{simple => rqs}
import spray.json.DefaultJsonProtocol

import scala.concurrent.Await
import scala.concurrent.duration._

/**
  * Testing integration with ArangoDB
  */
class IntegrationTest extends AkkaSpec {
  "To be easy to use Scarango driver in various abstraction levels" should {
    "have one-liner for final result (blocking is acceptable)" in withDriver { scarango =>
      assert(scarango.version().version === latestApiVersion)
    }
    "have future for common situations" in withFuturesDriver { scarango =>
      withDelay {
        scarango.version()
      } { raw =>
        assert(raw.version === latestApiVersion)
        assert(raw.server === "arango")
      }
    }
    "have flow to complete it by self" in {
      val context = defaultConfig
      implicit val system = context.actorSystem
      implicit val materializer = context.materializer
      val scarango = Scarango.newStreams(context)
      withDelay {
        scarango.version.runWith(Sink.head).flatMap(same => same)
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
    "can easily switch between abstraction levels" in {
      val databaseName1 = "one"
      val databaseName2 = "two"
      val databaseName3 = "three"
      val streamDriver: ScarangoStreams = Scarango.newStreams().withDatabase(databaseName1)
      val futuresDriver: ScarangoFutures = Scarango.newFutures().withDatabase(databaseName1)
      val awaitDriver: ScarangoAwait = Scarango.newAwait().withDatabase(databaseName1)
      assert(streamDriver.context.database === futuresDriver.context.database)
      assert(streamDriver.context.database === awaitDriver.context.database)
      assert(databaseName2 === streamDriver.withDatabase(databaseName2).toFutures.toAwait.context.database)
      assert(databaseName2 === awaitDriver.withDatabase(databaseName2).toFutures.toStreams.context.database)
      assert(databaseName3 === futuresDriver.withDatabase(databaseName3).toStreams.context.database)
      assert(databaseName3 === futuresDriver.withDatabase(databaseName3).toAwait.context.database)
    }
  }

  "To interact with database like a streams" should {
    "retrieve records in a (simulated) asynchronous way" in withStreamsDriver { scarango =>
      val collectionName = "with-data" + randomId
      implicit val context = scarango.context
      implicit val materializer = context.materializer
      part("Creating new elements") {
        scarango.toAwait.create(c.Collection(collectionName))
        for (i <- 1 to 20) {
          val rawData = s"""{"Hello": $i}"""
          scarango.toAwait.create(c.Document(rawData, collectionName))
        }
      }
      part("Retrieving stored elements") {
        val getAll = All(collectionName)
        val graph = scarango.query(getAll)
          .map(_.map(_.result))
          .flatMapConcat(Source.fromFuture)
          .expand(_.iterator)
        val resultViaIterator = Await.result(graph.map(_.prettyPrint).runReduce(_ + _), 4.seconds)
        val resultOfWhole = scarango.toAwait.query(getAll).result.map(_.prettyPrint).mkString("")
        val resultIterator = scarango.toAwait.iterator(getAll).map(_.prettyPrint).toList.mkString("")
        assert(resultViaIterator === resultOfWhole)
        assert(resultViaIterator === resultIterator)
      }
    }
  }

  "To cover ArangoDB API" when {
    "testing administration part" should {
      "get version of ArangoDB" in withFuturesDriver { scarango =>
        withDelay {
          scarango.version()
        } { raw =>
          assert(raw.version === latestApiVersion)
          assert(raw.server === "arango")
        }
      }
    }
    "testing database" should withDriver { scarango =>
      "be able to create and remove new database" in {
        val name = "db" + randomId
        part("Creating new database") {
          context(s"New database: $name")
          val response = scarango.create(c.Database(name))
          assert(response.result === true)
          assert(response.error === false)
          assert(response.code === HttpStatusCodes.created)
        }
        part("Removing database") {
          val response = scarango.delete(d.Database(name))
          assert(response.result === true)
          assert(response.error === false)
          assert(response.code === HttpStatusCodes.ok)
        }
        part("Checking in existing databases") {
          val databases = scarango.listDatabases()
          assert(databases.result.contains(name) === false)
          context(s"Databases after removal: ${databases.result}")
        }
      }
      "be able to list existing databases" in {
        val name = "db" + randomId
        part("Creating database") {
          scarango.create(c.Database(name))
          context(s"New database: $name")
        }
        part("Listing databases") {
          val databases = scarango.listDatabases()
          assert(databases.code === HttpStatusCodes.ok)
          assert(databases.error === false)
          assert(databases.result.contains(name))
          context(s"Databases: ${databases.result}")
        }
        part("Removing database") {
          scarango.delete(d.Database(name))
          context(s"Database removed: $name")
        }
      }
    }
    "testing collections" should withDriver { scarango =>
      "be able to create and remove new collection" in {
        val collectionName = "collection" + randomId
        part("Creating new collection") {
          context(s"New collection: $collectionName in _system")
          val createResponse = scarango.create(c.Collection(collectionName))
          assert(createResponse.name === collectionName)
          assert(createResponse.`type` === CollectionTypes.Document)
          assert(createResponse.error === false)
          assert(createResponse.code === HttpStatusCodes.ok)
          assert(createResponse.status === CollectionStatuses.Loaded)
        }
        part("Removing collection") {
          val collectionRemoved = scarango.delete(d.Collection(collectionName))
          assert(collectionRemoved.id.length > 1)
          assert(collectionRemoved.error === false)
          assert(collectionRemoved.code === HttpStatusCodes.ok)
          context(s"Removed collection: $collectionName in _system")
        }
      }
      "be able to create new collection in custom database" in {
        val databaseName = "db" + randomId
        val collectionName = "collection" + randomId
        part("Creating database") {
          val dbCreated = scarango.create(c.Database(databaseName))
          assert(dbCreated.result === true)
          context(s"New database: $databaseName")
        }
        part("Creating collection") {
          val inCreatedDb = scarango.withDatabase(databaseName)
          val collectionCreated = inCreatedDb.create(c.Collection(collectionName))
          assert(collectionCreated.error === false)
          context(s"New collection: $collectionName in $databaseName")
        }
        part("Removing database") {
          val dbRemoved = scarango.delete(d.Database(databaseName))
          assert(dbRemoved.result === true)
          context(s"Database removed: $databaseName")
        }
      }
      "be able to see collections inside _system database" in {
        val collectionName = "collection" + randomId
        part("Creating new collection") {
          scarango.create(c.Collection(collectionName))
          context(s"New collection: $collectionName in _system")
        }
        part("Listing existing collections") {
          val collections = scarango.listCollections()
          sub("Comparing basic response") {
            assert(collections.error === false)
            assert(collections.code === HttpStatusCodes.ok)
            assert(collections.collections.size > 1)
            context(s"Collections: ${collections.collections.map(_.name)}")
          }
          sub("Comparing newly created collection") {
            val createdCollection = byName(collections.collections, collectionName)
            assert(createdCollection.id.length > 1)
            assert(createdCollection.name === collectionName)
            assert(createdCollection.isSystem === false)
            assert(createdCollection.`type` === CollectionTypes.Document)
            assert(createdCollection.status === CollectionStatuses.Loaded)
          }
          sub("Comparing system collections") {
            val usersCollection = byName(collections.collections, "_users")
            assert(usersCollection.isSystem === true)
            assert(contains(collections.collections, "_graphs"))
            assert(contains(collections.collections, "_sessions"))
          }
        }
        part("Removing collection") {
          scarango.delete(d.Collection(collectionName))
          val collectionsLeft = scarango.listCollections()
          assert(contains(collectionsLeft.collections, collectionName) === false, "collection removed")
          context(s"Collections after removal: ${collectionsLeft.collections.map(_.name)}")
        }
      }
      "be able to see collections in custom database" in {
        val databaseName = "db" + randomId
        val collectionName = "collection" + randomId
        part("Creating new database") {
          scarango.create(c.Database(databaseName))
          context(s"New database: $databaseName")
        }
        part("In newly created database") {
          val inCreatedDb = scarango.withDatabase(databaseName)
          sub("Creating new collection") {
            inCreatedDb.create(c.Collection(collectionName))
            context(s"New collection: $collectionName in $databaseName")
          }
          val collections = subCommented("Listing collections") {
            inCreatedDb.listCollections()
          }
          sub("Comparing basic response from the list of database collections") {
            assert(collections.error === false)
            assert(collections.code === HttpStatusCodes.ok)
          }
          sub("Comparing newly created collection") {
            val createdCollection = byName(collections.collections, collectionName)
            assert(createdCollection.id.length > 1)
            assert(createdCollection.name === collectionName)
            assert(createdCollection.isSystem === false)
            assert(createdCollection.`type` === CollectionTypes.Document)
            assert(createdCollection.status === CollectionStatuses.Loaded)
          }
          sub("Comparing system collections") {
            assert(contains(collections.collections, "_graphs"))
            assert(contains(collections.collections, "_sessions"))
          }
        }
        part("Removing database") {
          val dbRemoved = scarango.delete(d.Database(databaseName))
          assert(dbRemoved.result === true)
          context(s"Database removed: $databaseName")
        }
      }
    }
    "testing documents" should withDriver { scarango =>
      "be able to create and list new documents" in {
        val collectionName = "with-data" + randomId
        part("Creating collection") {
          scarango.create(c.Collection(collectionName))
          context(s"New collection: $collectionName in _system")
        }
        part("Creating new documents") {
          for (i <- 1 to 5) {
            val rawData = s"""{"Hello": $i}"""
            val response = scarango.create(c.Document(rawData, collectionName))
            assert(response.error === false)
            assert(response._id === collectionName + "/" + response._key)
          }
        }
        part("Listing new documents") {
          val response = scarango.query(All(collectionName))
          assert(response.result.size === 5)
          for (document <- response.result) {
            val id = document.id
            val key = document.key
            assert(id === collectionName + "/" + key)
          }
          context(s"Documents (ids only): ${response.result.map(_.id)}")
        }
        part("Removing collection") {
          scarango.delete(d.Collection(collectionName))
          context(s"Collection removed: $collectionName")
        }
      }
      "be able to create and remove new documents" in {
        val collectionName = "with-data" + randomId
        part("Creating collection") {
          scarango.create(c.Collection(collectionName))
          context(s"New collection: $collectionName in _system")
        }
        val newDocument = commented("Creating new document") {
          val rawData = s"""{"Hello": "New"}"""
          scarango.create(c.Document(rawData, collectionName))
        }
        context(s"New document created: ${newDocument._id}")
        part("Reading document by key") {
          val document = scarango.get(g.Document(collectionName, newDocument._key))
          assert(newDocument._key === document.key)
          assert(rqs.Document.jsToString(document.fields("Hello")) === "New")
          assert(document.compactPrint === s"""{"Hello":"New","_id":"${document.id}","_rev":"${document.revision}","_key":"${document.key}"}""")
          context(s"Document content: $document")
          sub("Converting to object") {
            case class HelloDocument(Hello: String, _id: String)
            import DefaultJsonProtocol._
            implicit def HelloDocumentFormatter = jsonFormat2(HelloDocument)
            val helloDocument = document.convertTo[HelloDocument]
            assert(helloDocument.Hello === "New")
            assert(helloDocument._id === newDocument._id)
            context(s"Object: $helloDocument")
          }
        }
        part("Removing newly created document") {
          val deleted = scarango.delete(d.Document(collectionName, newDocument._key))
          assert(deleted.error === false)
          assert(deleted._id === collectionName + "/" + deleted._key)
        }
        part("Removing collection") {
          scarango.delete(d.Collection(collectionName))
          context(s"Collection removed: $collectionName")
        }
      }
    }
  }

  "To be suitable for production" should {
    "restrict database to specific user" in withDriver { scarango =>
      val webApp = c.User("webapp", "cyFAP7iwCPy8gh3WZHHG", active = true)
      val backdoor = c.User("backdoor", "123456", active = false)
      val other = c.User("other", "11111111", active = true)
      val databaseName = "prod-like" + randomId
      val database = commented("Create database with user credentials") {
        context(s"New database: $databaseName")
        scarango.create(c.Database(databaseName, List(webApp, backdoor)))
      }
      part("Testing database creation response") {
        assert(database.code === HttpStatusCodes.created)
        assert(database.error === false)
        assert(database.result === true)
      }
      part("New database should not be visible to default user") {
        val databases = scarango.listDatabases()
        assert(databases.result.contains(databaseName))
      }
      val asWebApp = scarango.withDatabase(databaseName).withUser(webApp)
      part("Connecting with webApp user and listing collections"){
        val collections = asWebApp.listCollections()
        assert(contains(collections.collections, "_users") === true)
        val users = asWebApp.query(All("_users"))
        assert(users.result.size === 2)
        val backDoorUser = users.result.filter(o => userField(o) == backdoor.username).head
        val webAppUser = users.result.filter(o => userField(o) == webApp.username).head
        context(s"User data: $backDoorUser")
        context(s"User data: $webAppUser")
      }
      val asBackDoor = scarango.withDatabase(databaseName).withUser(backdoor)
      part("Not allowed to connect with backDoor user, which is not active") {
        context("Assuming disable-authentication=false")
        intercept[Exception] {
          asBackDoor.listCollections()
        }
      }
      val asOther = scarango.withDatabase(databaseName).withUser(other)
      part("Not allowed to connect with other user") {
        context("Assuming disable-authentication=false")
        intercept[Exception] {
          asOther.listCollections()
        }
      }
      val asRoot = scarango.withDatabase(databaseName)
      part("Not allowed to connect with root") {
        context("Assuming disable-authentication=false")
        intercept[Exception] {
          asRoot.listCollections()
        }
      }

      part("Removing database") {
        scarango.delete(d.Database(databaseName))
        context(s"Database removed: $databaseName")
      }
    }
  }
}
