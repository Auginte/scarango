package com.auginte.scarango.common

import spray.http.BasicHttpCredentials
import spray.http.HttpHeaders.Authorization

/**
 * Wrapper for login
 */
class Authorisation(val user: String, val password: String) {
  def http = Authorization(BasicHttpCredentials(user, password))
}

object Authorisation {
  def default = new Authorisation("root", "")
}
