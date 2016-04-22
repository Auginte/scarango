package com.auginte.scarango

import akka.stream.scaladsl.{Sink, Source}
import com.auginte.scarango.helpers.AkkaSpec

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
    "have flow to compleate it by self" in {
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
    "have access to all parts to create flow from scrach" in {
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
  }
}