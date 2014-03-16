import org.jivesoftware.smack.{ConnectionConfiguration, XMPPConnection}
import org.scalatest.{Matchers, FunSpec}

class TopicTrackerTest extends FunSpec with Matchers {
  it("passes") {
    1 should equal(1)
  }

  it("connects to the XMPP server") {
    val config = new ConnectionConfiguration("jabber.org", 5222)
    val connection = new XMPPConnection(config)

    connection.connect()
    connection.login("android-radiot@jabber.org", "android-radiot")

    connection.disconnect()
  }

  it("joins the chat") {
    val config = new ConnectionConfiguration("jabber.org", 5222)
    val connection = new XMPPConnection(config)

    connection.connect()
    connection.login("android-radiot@jabber.org", "android-radiot")

    connection.disconnect()
  }
}
