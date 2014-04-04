package org.dandelion.radiot.server

import org.jivesoftware.smack.{PacketListener, SmackConfiguration, XMPPConnection, ConnectionConfiguration}
import org.jivesoftware.smackx.muc.{DiscussionHistory, MultiUserChat}
import org.jivesoftware.smack.packet.{Message, Packet}

class JabberChat(val username: String, val password: String) {
  private val Room = "online@conference.precise64"

  private val connection = new XMPPConnection(new ConnectionConfiguration("localhost", 5222))
  private val chat = new MultiUserChat(connection, Room)

  private val messageListener = new PacketListener {
    override def processPacket(packet: Packet) = packet match {
      case msg: Message => onMessage(msg.getBody)
      case _ =>
    }
  }


  def connect() {
    logIntoServer()
    joinChat()
  }

  def disconnect() {
    if (chat.isJoined) chat.leave()
    if (connection.isConnected) connection.disconnect()
  }

  def sendMessage(msg: String) = chat.sendMessage(msg)

  def onMessage(msg: String) {}

  private def logIntoServer() {
    if (!connection.isConnected) connection.connect()
    if (!connection.isAuthenticated) connection.login(username, password)
  }

  private def joinChat() {
    chat.join(username, null, emptyHistory, SmackConfiguration.getPacketReplyTimeout)
    chat.addMessageListener(messageListener)
  }

  private def emptyHistory = {
    val history = new DiscussionHistory()
    history.setMaxStanzas(0)
    history
  }

}
