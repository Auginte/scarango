package com.auginte.scarango.errors

/**
 * Marker for all ArangoDb wrapper related errors.
 *
 * Those are sent back to wrapper client (business application)
 */
abstract class ScarangoError(message: String) extends Exception(message)