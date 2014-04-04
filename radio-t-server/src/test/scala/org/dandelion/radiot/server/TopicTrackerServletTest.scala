package org.dandelion.radiot.server


class TopicTrackerServletTest extends RadiotServerSpec {
  override val servlet = new TopicTrackerServlet("/chat")
  addServlet(servlet, servlet.root + "/*")

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



