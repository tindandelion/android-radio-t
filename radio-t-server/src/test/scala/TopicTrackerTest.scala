import org.jivesoftware.smack.packet.{Message, Packet}
import org.jivesoftware.smack.{PacketListener, ConnectionConfiguration, XMPPConnection}
import org.jivesoftware.smackx.muc.MultiUserChat
import org.scalatest.{BeforeAndAfter, Matchers, FunSpec}
import scala.collection.mutable

class ChatMessageListener extends PacketListener {
  val messages: mutable.MutableList[Message] = new mutable.MutableList[Message]

  def processPacket(p: Packet) = {
    val msg: Message = p.asInstanceOf[Message]
    messages += msg
  }

  def receivedMessages = !messages.isEmpty
}

class TopicTrackerTest extends FunSpec with Matchers with BeforeAndAfter {
  val config = new ConnectionConfiguration("jabber.org", 5222)
  val connection = new XMPPConnection(config)

  before {
    connection.connect()
    connection.login("android-radiot@jabber.org", "android-radiot")
  }

  after {
    connection.disconnect()
  }

  it("joins the chat") {
    val chat = new MultiUserChat(connection, "online@conference.radio-t.com")

    chat.join("android-radiot")
    chat should be('joined)

    chat.leave()
  }

  it("listens for the messages") {
    val chat = new MultiUserChat(connection, "online@conference.radio-t.com")
    val listener = new ChatMessageListener

    chat.addMessageListener(listener)

    chat.join("android-radiot")
    chat should be('joined)

    listener should be('receivedMessages)

    chat.leave()
  }
}
