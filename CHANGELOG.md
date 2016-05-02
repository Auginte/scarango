# Unreleased

You can expect braking changes until `1.*` version is released.

See `Issues` in Github for future plans and your suggestions.

# v0.3

## v0.3.1 Reactive Streams: Minimal functionality


**Main features**

 * Rewritten to use Reactive streams (`akka http`)
 * Tried to cover as much ArangoDB API as `v0.2.4`
 * Updated to newest `Scala` and `ArangoDB` versions

Read more: [#1](../../issues/1)

**Compatibility**: **Braking changes**

 * Rewritten from the ground
 * Dropped `scarango_macros` - not as developer friendly as expected 


# v0.2

## v0.2.4 Users: creation and authentication

**Main features**

 * Create database with users. Users can also include extra fields
 * Better credentials management: when working with same or multiple databases/collections/authentications

**Main fixes**

 * Dependency to scarango-macros

**Compatibility**: **Not backward compatible**

 * Order of arguments and currying in `request`, `response` objects have changed
 * Tests expects ArangoDb `2.6.8`

## v0.2.3 Most basic functionality for database, colletion and documents

**Main features**

 * Database: Create, List, Remove
 * Collection: Create, Read-basics, Remove
 * Document: Create, List, Read, Remove
 * ArangoDB: GetVersion
