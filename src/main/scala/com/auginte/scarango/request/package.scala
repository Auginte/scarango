package com.auginte.scarango

/**
 * Package contains all available requests to [[com.auginte.scarango.Scarango]] actor.
 *
 *
 * ==Organisation==
 *
 * From class hierarchy you can see, that everything is organised into:
   - [[com.auginte.scarango.request.Request]]
      - [[com.auginte.scarango.request.CreateRequest]]
      - [[com.auginte.scarango.request.GetRequest]]
      - [[com.auginte.scarango.request.RemoveRequest]]
 *
 *
 * ==Example of usage==
 *
 * {{{
 *  import com.auginte.scarango.request._
 *
 *  val db = system.actorOf(Props[Scarango])
 *  db ! GetVersion
 *  db ! CreateDatabase(dbName)
 *  db ! CreateCollection(collectionName)
 *  db ! RemoveCollection(collectionName)
 *  db ! GetDatabases
 *  db ! RemoveDatabase(dbName)
 *  db ! GetDatabases
 * }}}
 *
 * Also tests could be used as examples: `com.auginte.scarango.IntegrationTest`
 *
 * ==See also==
 *
 * Responses from [[com.auginte.scarango.Scarango]] can be one of (or child classes):
    - [[com.auginte.scarango.response.Response]]
    - [[com.auginte.scarango.errors.ScarangoError]]
 */
package object request