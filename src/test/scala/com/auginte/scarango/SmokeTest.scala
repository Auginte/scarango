package com.auginte.scarango

import com.auginte.scarango.request.parts.Authorisation
import org.scalatest.{ShouldMatchers, WordSpec}

/**
 * Testing Travis integration
 */
class SmokeTest extends WordSpec with ShouldMatchers {
  "Client with default implementation" should {
    "have default root credentials" in {
      Authorisation.default should be (new Authorisation("root", ""))
    }
  }
}
