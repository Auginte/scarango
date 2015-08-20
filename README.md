Just another Scala driver for ArangoDB
======================================

[![Build Status](https://secure.travis-ci.org/aurelijusb/scarango.png?branch=master)](http://travis-ci.org/aurelijusb/scarango)

Actor based Scala driver/client for ArangoDB.
This driver is based on ArangoDB REST API, spray.io.

Stability
---------

**This is early alpha.
Do not expect much**

Why another driver
------------------

This client/driver concentrates on faster/easier development of

* Akka based applications
* Graph intensive applications

Meaning, not the coverage of newest API changes or fancy ORM.

Similar projects
----------------

* https://github.com/CharlesAHunt/proteus
* https://github.com/sumito3478/scarango

Related
-------

* https://github.com/arangodb/arangodb/

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