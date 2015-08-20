package com.auginte.scarango

import com.auginte.scarango.common.Authorisation
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
