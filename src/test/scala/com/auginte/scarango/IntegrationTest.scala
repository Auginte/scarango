package com.auginte.scarango

import com.auginte.scarango.helpers.AkkaSpec

/**
  * Testing integration with ArangoDB
  */
class IntegrationTest extends AkkaSpec {
  "In environment with real ArangoDB instance, driver" should {
    "get version  of ArangoDB" in {
      val scarango = new Scarango(defaultConfig)
      withDelay {
        scarango.version()
      } { raw =>
        assert(raw.version === latestApiVersion)
        assert(raw.server === "arango")
      }
    }
  }
}