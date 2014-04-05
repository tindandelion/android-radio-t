package org.dandelion.radiot.server

case class JabberConfig(server: String, username: String, password: String, room: String) {
  val port = 5222
}
