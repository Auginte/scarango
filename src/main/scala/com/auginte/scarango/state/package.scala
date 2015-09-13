package com.auginte.scarango

/**
 * Package contains types and helpers to simulate state between request-response of ArangoDB
 *
 * == Use ceses ==
 *
 * * Connect to needed database and do all operations on that 1 database. No need to write `dbName` every time
 * * Load collection and do multiple operations on that 1 collection. No need to write `collectionName` every time
 *
 */
package object state {
  type DatabaseName = String
  type CollectionName = String
}
