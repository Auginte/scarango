package com.auginte.scarango.state

import akka.http.scaladsl.model.headers.{BasicHttpCredentials, RawHeader}

/**
 * Wrapper for login
 */
class Authorisation(val user: String, val password: String) {
  lazy val http = BasicHttpCredentials(user, password)
  lazy val header = RawHeader("Authorization", http.scheme() + " " + http.token())

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

//  def forUser(user: User) = new Authorisation(user.name, user.password)
}
