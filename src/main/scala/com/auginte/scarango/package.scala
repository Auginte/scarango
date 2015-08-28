package com.auginte

/**
 * ArangoDB Scala driver. Use [[com.auginte.scarango.Scarango]] Actor as a wrapper to ArangoDB REST API
 *
 * There are 3 types of supported actor messages (requests/responses) grouped in each package:
    - [[com.auginte.scarango.request]] - Messages to send from you (client) to driver (wrapper)
    - [[com.auginte.scarango.response]] - Messages of success sent back from driver (wrapper) to you (client)
    - [[com.auginte.scarango.errors]] - Messages of failure sent back from driver (wrapper) to you (client)
 */
package object scarango {
  /**
   * Latest version of ArangoDB that is supported by the driver
   *
   * Tests are depending on that driver
   */
  val latestApiVersion = "2.6.7"
}