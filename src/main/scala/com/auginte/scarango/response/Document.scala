package com.auginte.scarango.response

import com.auginte.scarango.state.DatabaseName

/**
 * Raw representation of saved document
 */
case class Document(json: String, id: String, database: DatabaseName) extends Data