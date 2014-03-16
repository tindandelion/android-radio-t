import org.jivesoftware.smack.{ConnectionConfiguration, XMPPConnection}
import org.jivesoftware.smackx.muc.MultiUserChat
import org.scalatest.{BeforeAndAfter, Matchers, FunSpec}

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
}
