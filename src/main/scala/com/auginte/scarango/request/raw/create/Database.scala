package com.auginte.scarango.request.raw.create

/**
  * Create Database
  * POST:  /_api/database
  */
case class Database(name: String, users: Seq[User] = List())