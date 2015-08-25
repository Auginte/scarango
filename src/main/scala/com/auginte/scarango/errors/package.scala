package com.auginte.scarango

/**
 * Package contains all errors returned from [[com.auginte.scarango.Scarango]] actor.
 *
 *
 * ==Organisation==
 *
 * From class hierarchy you can see, that everything is organised into:
   - [[com.auginte.scarango.errors.ScarangoError]]
      - [[com.auginte.scarango.errors.UnprocessedRequest]]
      - [[com.auginte.scarango.errors.UnexpectedRequest]]
 *
 *
 * ==Example of usage==
 *
 * {{{
 *  import com.auginte.scarango.errors.ScarangoError
 *
 *  class Client extends Actor {
 *    override def receive: Receive = {
 *
 *      case e: ScarangoError =>
 *          println("ERROR " + e.getMessage)
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
 * All succesful responses should be children of [[com.auginte.scarango.response.Response]]
 * Actor could also return child of [[com.auginte.scarango.errors.ScarangoError]]
 */
package object errors {

}
