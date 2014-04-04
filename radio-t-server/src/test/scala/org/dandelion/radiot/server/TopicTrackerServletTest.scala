package org.dandelion.radiot.server

import org.scalatest.{BeforeAndAfter, Matchers}


class TopicTrackerServletTest extends RadiotServerSpec
with BeforeAndAfter with Matchers {
  override val servlet = new TopicTrackerServlet("/chat")

  addServlet(servlet, servlet.root + "/*")

  before {
    client.start()
    client.connect(socket, serverUrl)
    eventually { socket should be('connected) }

    servlet.connectToChat()
  }

  after {
    client.stop()
    servlet.disconnectFromChat()
  }

  it("answers a simple request") {
    get("/chat") {
      status should equal(200)
      body should equal("Hello world!")
    }
  }

  it("receives current topic after connection is established") {
    topicShouldBe("Default topic")
  }

  it("changes a topic by a message from the chat") {
    val newTopic = "New topic to discuss"

    sendMessageToChat(newTopic)

    topicShouldBe(newTopic)
  }


  def sendMessageToChat(msg: String) {
    new JabberChat("jc-radio-t", "password") {
      connect()
      sendMessage(msg)
      disconnect()
    }
  }

}



