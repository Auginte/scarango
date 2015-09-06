Just another Scala driver for ArangoDB
======================================

[![Build Status](https://secure.travis-ci.org/aurelijusb/scarango.png?branch=master)](http://travis-ci.org/aurelijusb/scarango)

Actor based Scala driver/client for ArangoDB.
This driver is based on [ArangoDB](https://www.arangodb.com/) REST API and spray.io.

Stability
---------

**This is early alpha**

* **Database**: Create, List, Remove, ~~by user~~, ~~with user data~~ 
* **Collection**: Create, ~~List~~, Read (status, type, ~~properties~~, ~~count~~, ~~statistics~~), Remove, ~~(un)laod~~, ~~truncate~~, ~~rotate~~, ~~rename~~
* **Document**: Create, List, Read, ~~Update~~, Remove, ~~Test~~
* ~~Query, Cursor, Explain~~
* ~~Graph, edge, edges~~~
* ~~Batch, Index~~
* ~~Export, replicate~~
* Version, ~~WAL, System, Tasks, Log~~

See [integration tests](src/test/scala/com/auginte/scarango/IntegrationTest.scala)
for details of covered functionality and usage examples  

Why another driver
------------------

This client/driver concentrates on faster/easier development of

* Akka/spray based applications
* Graph intensive applications

Meaning, not the coverage of newest API changes or fancy ORM.

Architecture concepts
---------------------

* Interacting with ArangoDB via Scarango Actor (reuse open HTTP connection)
* All operations/actor messages grouped into requests, response and error packages
* `request.groups` traits used for Functional grouping

Similar projects
----------------

* https://github.com/CharlesAHunt/proteus
* https://github.com/sumito3478/scarango

Run
---

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