Just another Scala driver for ArangoDB
======================================

[![Build Status](https://api.travis-ci.org/Auginte/scarango.png?branch=master)](http://travis-ci.org/Auginte/scarango)
[![Maven Central](https://maven-badges.herokuapp.com/maven-central/com.auginte/scarango_2.11/badge.svg)](http://search.maven.org/#artifactdetails|com.auginte|scarango_2.11|0.3.1|)

Reactive streams based Scala driver/client for ArangoDB.
This driver is based on [ArangoDB](https://www.arangodb.com/) REST API and Akka streams and spray JSON.

Outdated code NOTICE
====================

:exclamation: As this project is not actively maintained, you may consider using: https://github.com/outr/scarango ([#5](https://github.com/Auginte/scarango/issues/5))

Stability
---------

**This is early version**. Functionality:

 * Administration: version, ~~statistics~~, ~~tasks~~, ~~etc~~
 * AQL: ~~CRUD~~, ~~etc~~
 * Bulk: ~~exectute~~, ~~import~~, ~~export~~, ~~etc~~
 * Cluster: ~~CRUD~~, ~~etc~~ 
 * Collections: Create, List, ~~Get~~, ~~Update~~, Delete, ~~etc~~ 
 * Cursors: ~~Create~~, ~~Delete~~, ~~Read~~
 * Database: Create (with users), List, ~~Get~~, Delete
 * Documents: Create, Get, ~~List~~, Replace, ~~Patch~~, Delete, ~~etc~~  
 * Graph: ~~CRUD~~, ~~etc~~ 
 * Graph edges: ~~CRUD~~, ~~etc~~ 
 * Graph Traversal: ~~Exectute~~ 
 * Indexes: ~~Create(SkipList, CapacityConstrain, Fulltext, General, Geo, Hash), Read, Get, Delete~~  
 * Job: ~~List, Get, etc~~ 
 * Replication: ~~CRUD~~, ~~etc~~
 * Simple Queries: Get(All, ~~any~~, ~~byExample~~, ~~etc~~), ~~Remove~~, ~~Update~~ 
 * Transactions: ~~Execute~~ 
 * User handling: ~~Create, List, Fetch, Replace, Remove~~ 
 * Write ahead log: ~~Get, Update Flush~~  

Usage
-----

You may need to add the Sonatype nexus to your resolvers:

```scala
resolvers += "Sonatype OOS" at "https://oss.sonatype.org/content/repositories/releases"
```

sbt:
```scala
libraryDependencies += "com.auginte" % "scarango_2.11" % "0.3.1"
```

Maven:
```xml
<dependency>
  <groupId>com.auginte</groupId>
  <artifactId>scarango_2.11</artifactId>
  <version>0.3.1</version>
</dependency>
```
or [other](http://search.maven.org/#artifactdetails|com.auginte|scarango_2.11|0.3.1|)

Examples
--------

* Clone [Example project](https://github.com/aurelijusb/scarango-example)
* See [integration tests](src/test/scala/com/auginte/scarango/IntegrationTest.scala) 

Why another driver
------------------

This client/driver concentrates on faster/easier development of

* Reactive streams/non-blocking applications
* Graph intensive applications

*Development still in progress*

Older versions
--------------

[v0.2.4](https://github.com/Auginte/scarango/tree/v0.2.4) was last version,
that was based on Spray 1.x version.

All later versions are based on Akka Http (Spray 2.x)
and are **not** back-compatible with Spray 1.x

For details see [Changelog](CHANGELOG.md) or use `git diff` :smirk:

Similar projects
----------------

* https://github.com/CharlesAHunt/proteus
* https://github.com/sumito3478/scarango

Run/Develop driver itself
-------------------------

Assuming, that ArangoDb is installed on http://127.0.0.1:8529 and `disable-authentication=false`

Run once:

```
sbt run
```

During development

```
sbt "~re-start"
```

Test
----

```
sbt test
```

License
-------

[Apache 2.0](LICENSE)
