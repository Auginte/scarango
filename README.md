Just another Scala driver for ArangoDB
======================================

[![Build Status](https://secure.travis-ci.org/aurelijusb/scarango.png?branch=master)](http://travis-ci.org/aurelijusb/scarango)

Actor based Scala driver/client for ArangoDB.
This driver is based on [ArangoDB](https://www.arangodb.com/) REST API and spray.io.

Stability
---------

**This is early version**. Functionality:

* **Database**: Create, List, Remove, ~~by user~~, ~~with user data~~ 
* **Collection**: Create, ~~List~~, Read (status, type, ~~properties~~, ~~count~~, ~~statistics~~), Remove, ~~(un)laod~~, ~~truncate~~, ~~rotate~~, ~~rename~~
* **Document**: Create, List, Read, ~~Update~~, Remove, ~~Test~~
* ~~Query, Cursor, Explain~~
* ~~Graph, edge, edges~~~
* ~~Batch, Index~~
* ~~Export, replicate~~
* Version, ~~WAL, System, Tasks, Log~~

Usage
-----

You may need to add the Sonatype nexus to your resolvers:

```scala
resolvers += "Sonatype OOS" at ""https://oss.sonatype.org/content/repositories/releases"
```

sbt:
```scala
libraryDependencies += "com.auginte" % "scarango_2.11" % "0.2.3"
```

Maven:
```xml
<dependency>
  <groupId>com.auginte</groupId>
  <artifactId>scarango_2.11</artifactId>
  <version>0.2.3</version>
  <classifier>sources</classifier>
</dependency>
```
or [other](http://search.maven.org/#artifactdetails|com.auginte|scarango_2.11|0.2.3|)

Examples
--------

* Clone [Example project](https://github.com/aurelijusb/scarango-example)
* See [integration tests](src/test/scala/com/auginte/scarango/IntegrationTest.scala) 

Why another driver
------------------

This client/driver concentrates on faster/easier development of

* Akka/spray based applications
* Graph intensive applications

*Development still in progress*

Architecture concepts
---------------------

* Interacting with ArangoDB via Scarango Actor (reuse open HTTP connection)
* All operations/actor messages grouped into requests, response and error packages

Similar projects
----------------

* https://github.com/CharlesAHunt/proteus
* https://github.com/sumito3478/scarango

Run/Develop driver itself
-------------------------

Assuming, that ArangoDb is installed on http://127.0.0.1:8529

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