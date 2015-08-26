package com.auginte.scarango

/**
 * Package contains all available succesful responses from [[com.auginte.scarango.Scarango]] actor.
 *
 *
 * ==Organisation==
 *
 * From class hierarchy you can see, that everything is organised into:
   - [[com.auginte.scarango.response.Response]]
      - [[com.auginte.scarango.response.Created]]
      - [[com.auginte.scarango.response.Data]]
      - [[com.auginte.scarango.response.Removed]]
 *
 * There is special use case for [[com.auginte.scarango.response.Identifiable]]
 *
 * ==Example of usage==
 *
 * {{{
 *  import com.auginte.scarango.responses._
 *
 *  class Client extends Actor {
 *    override def receive: Receive = {
 *
 *      case v: Version =>
 *        println("Got version: " + v.version)
 *
 *      case response.Identifiable(d: Databases, id, _, _, _) if id == "database removed" =>
 *        println("Created: " + d.name)
 *
 *    }
 *  }
 * }}}
 *
 * Also tests could be used as examples: `com.auginte.scarango.IntegrationTest`
 *
 * ==See also==
 *
 * Communicating with [[com.auginte.scarango.Scarango]] actor.
 * All request should be children of [[com.auginte.scarango.request.Request]]
 * Actor could also return child of [[com.auginte.scarango.errors.ScarangoError]]
 */
package object response