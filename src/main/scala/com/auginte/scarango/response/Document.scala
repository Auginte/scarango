package com.auginte.scarango.response

/**
 * Raw representation of saved document
 */
case class Document(json: String, id: String, database: String) extends Data