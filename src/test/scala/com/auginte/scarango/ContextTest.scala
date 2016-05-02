package com.auginte.scarango

import com.auginte.scarango.helpers.AkkaSpec
import com.auginte.scarango.request.raw.{create => c, delete => d}
import com.auginte.scarango.state.Authorisation

/**
  * Unit tests for context/settings
  */
class ContextTest extends AkkaSpec {
  "When using Scaranog Context, user" should {
    "be able to create driver with default context" in {
      val context = new Context()()
      assert(context.endpoint === Context.defaultEndpoint)
      assert(context.authorisation === Authorisation.default)
      assert(context.database === Context.defaultDatabase)
      assert(context.actorSystem === Context.defaultActorSystem)
      assert(context.materializer === Context.defaultMaterializer)
      assert(context.waitTime === Context.defaultWaitDuration)
    }
    "be able to derive context for different database" in {
      val context = new Context()()
      assert(context.database === Context.defaultDatabase)
      val customContext = context.withDatabase("new")
      assert(customContext.database === "new")
      assert(customContext.database !== context.database)
    }
  }
}