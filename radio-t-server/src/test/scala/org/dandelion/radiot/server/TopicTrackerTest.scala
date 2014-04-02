package org.dandelion.radiot.server

import org.jivesoftware.smack.packet.{Message, Packet}
import org.jivesoftware.smack.{PacketListener, ConnectionConfiguration, XMPPConnection}
import org.jivesoftware.smackx.muc.MultiUserChat
import org.scalatest.{Ignore, BeforeAndAfter, Matchers, FunSpec}
import scala.collection.mutable

class ChatMessageListener extends PacketListener {
  val messages: mutable.MutableList[Message] = new mutable.MutableList[Message]

  @Override
  def processPacket(p: Packet) = {
    val msg: Message = p.asInstanceOf[Message]
    println("Message " + msg.getBody)
    messages += msg
  }

  def receivedMessages = !messages.isEmpty
}

object TopicTracker {
  private val Server = "localhost"
  private val Username = "android-radiot"
  private val Password = "android-radiot"
  private val Room = "online@conference.precise32"
  private val Nickname = "android-radio-t"


  private val connection = new XMPPConnection(new ConnectionConfiguration(Server, 5222))
  private val chat = new MultiUserChat(connection, TopicTracker.Room)

  def connect() {
    connection.connect()
    connection.login(Username, Password)
    chat.join(Nickname)
  }

  def disconnect() {
    chat.leave()
    connection.disconnect()
  }

  def isJoined = chat.isJoined

  def addListener(listener: PacketListener) {
    chat.addMessageListener(listener)
  }
}

@Ignore
class TopicTrackerTest extends FunSpec with Matchers with BeforeAndAfter {
  val tracker = TopicTracker

  before {
    tracker.connect()
  }

  after {
    tracker.disconnect()
  }

  it("joins the chat at start") {
    tracker should be('joined)
  }
}
