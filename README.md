Just another Scala driver for ArangoDB
======================================

[![Build Status](https://secure.travis-ci.org/Auginte/scarango.png?branch=master)](http://travis-ci.org/Auginte/scarango)
[![Maven Central](https://maven-badges.herokuapp.com/maven-central/com.auginte/scarango_2.11/badge.svg)](http://search.maven.org/#artifactdetails|com.auginte|scarango_2.11|0.2.4|)

Reactive streams based Scala driver/client for ArangoDB.
This driver is based on [ArangoDB](https://www.arangodb.com/) REST API and Akka streams.

Stability
---------

**Core parts are being refactored: For stable versions: use `master` branch**

**This is early version**. Functionality:

* Database: Create, ~~List~~, Remove, ~~by user, with user data~~ 
* Collection: Create, List, ~~Read (status, type, properties, count, statistics)~~, Remove, ~~(un)laod~~, ~~truncate~~, ~~rotate~~, ~~rename~~
* Document: Create, List, ~~Read~~, ~~Update~~, Remove, ~~Test~~
* Query, ~~Cursor, Explain~~ Simple
* ~~Graph, edge, edges~~
* ~~Batch, Index~~
* ~~Export, replicate~~
* Version, ~~WAL, System, Tasks, Log~~

Usage
-----

You may need to add the Sonatype nexus to your resolvers:

```scala
resolvers += "Sonatype OOS" at "https://oss.sonatype.org/content/repositories/releases"
```

sbt:
```scala
libraryDependencies += "com.auginte" % "scarango_2.11" % "0.3.1-SNAPSHOT"
```

Maven:
```xml
<dependency>
  <groupId>com.auginte</groupId>
  <artifactId>scarango_2.11</artifactId>
  <version>0.3.1-SNAPSHOT</version>
</dependency>
```
or [other](http://search.maven.org/#artifactdetails|com.auginte|scarango_2.11|0.2.4|)

Examples
--------

* Clone [Example project](https://github.com/aurelijusb/scarango-example)
* See [integration tests](src/test/scala/com/auginte/scarango/IntegrationTest.scala) 

Why another driver
------------------

This client/driver concentrates on faster/easier development of

* Reactive streams/non-blocking oriented applications
* Graph intensive applications

*Development still in progress*

Older versions
--------------

[v0.2.4](https://github.com/Auginte/scarango/tree/v0.2.4) was last version,
that was based on Spray 1.x version.

All later versions are based on Akka Http (Spray 2.x)
and are **not** back compatible with Spray 1.x

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
