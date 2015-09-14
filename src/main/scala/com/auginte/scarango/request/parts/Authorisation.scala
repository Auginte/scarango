package com.auginte.scarango.request.parts

import spray.http.BasicHttpCredentials
import spray.http.HttpHeaders.Authorization

/**
 * Wrapper for login
 */
class Authorisation(val user: String, val password: String) {
  def http = Authorization(BasicHttpCredentials(user, password))

  override def equals(other: Any): Boolean = other match {
    case that: Authorisation =>
      (that canEqual this) &&
        user == that.user &&
        password == that.password
    case _ => false
  }

  override def hashCode(): Int = {
    val state = Seq(user, password)
    state.map(_.hashCode()).foldLeft(0)((a, b) => 31 * a + b)
  }

  private def canEqual(other: Any): Boolean = other match {
    case a: Authorisation => true
    case _ => false
  }
}

object Authorisation {
  def default = new Authorisation("root", "")

  def forUser(user: User) = new Authorisation(user.name, user.password)
}
