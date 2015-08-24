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
      -  [[com.auginte.scarango.response.Data]]
      - [[com.auginte.scarango.response.Removed]]
 *
 *
 * ==Example of usage==
 *
 * {{{
 *  import com.auginte.scarango.responses._
 *
 *  class Client extends Actor {
 *    override def receive: Receive = {
 *
 *      case ResponseIdentifier(v: Version, _) =>
 *        println("Got version: " + v.version)
 *
 *      case ResponseIdentifier(d: DatabaseCreated, _) =>
 *        println("Created: " + d.name)
 *
 *    }
 *  }
 * }}}
 *
 *
 * ==See also==
 *
 * Communicating with [[com.auginte.scarango.Scarango]] actor.
 * All request should be children of [[com.auginte.scarango.request.Request]]
 * Actor could also return child of [[com.auginte.scarango.errors.ScarangoError]]
 */
package object response