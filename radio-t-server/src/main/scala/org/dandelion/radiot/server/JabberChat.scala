package org.dandelion.radiot.server

import org.jivesoftware.smack.{PacketListener, SmackConfiguration, XMPPConnection, ConnectionConfiguration}
import org.jivesoftware.smackx.muc.{DiscussionHistory, MultiUserChat}
import org.jivesoftware.smack.packet.{Message, Packet}

class JabberChat(val cfg: JabberConfig) {

  private val connection = new XMPPConnection(new ConnectionConfiguration(cfg.server, cfg.port))
  private val chat = new MultiUserChat(connection, cfg.room)

  private val messageListener = new PacketListener {
    override def processPacket(packet: Packet) = packet match {
      case msg: Message => onMessage(msg.getBody)
      case _ =>
    }
  }

  def connect() {
    logIntoServer(cfg.username, cfg.password)
    joinChat(cfg.username)
  }

  def disconnect() {
    if (chat.isJoined) chat.leave()
    if (connection.isConnected) connection.disconnect()
  }

  def sendMessage(msg: String) = chat.sendMessage(msg)

  def onMessage(msg: String) {}

  private def logIntoServer(username: String, password: String) {
    if (!connection.isConnected) connection.connect()
    if (!connection.isAuthenticated) connection.login(username, password)
  }

  private def joinChat(username: String) {
    chat.join(username, null, emptyHistory, SmackConfiguration.getPacketReplyTimeout)
    chat.addMessageListener(messageListener)
  }

  private def emptyHistory = {
    val history = new DiscussionHistory()
    history.setMaxStanzas(0)
    history
  }

}
