package org.dandelion.radiot.server


class TopicTrackerServletTest extends RadiotServerSpec {
  val localChatConfig = JabberConfig(
    server = "localhost",
    username = "android-radiot",
    password = "password",
    room = "online@conference.precise64")

  val localAdminConfig = localChatConfig.copy(username = TopicTrackerServlet.TopicStarter)

  override val servlet = new TopicTrackerServlet("/chat", localChatConfig)
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

    sendMessageToChat("--> " + newTopic)
    topicShouldBe(newTopic)
  }

  def sendMessageToChat(msg: String) {
    new JabberChat(localAdminConfig) {
      connect { (_, _) => }
      sendMessage(msg)
      disconnect()
    }
  }

}



