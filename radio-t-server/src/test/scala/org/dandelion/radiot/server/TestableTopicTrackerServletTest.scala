package org.dandelion.radiot.server

import org.scalatest.Matchers


class TestableTopicTrackerServletTest extends RadiotServerSpec
with Matchers  {
  override val servlet = new TestableTopicTrackerServlet("/testing/chat")

  addServlet(servlet, servlet.root + "/*")


  before {
    client.start()
    client.connect(socket, serverUrl)
    eventually { socket should be('connected) }
  }

  after {
    client.stop()
  }

  it("changes a topic by POST request") {
    val newTopic = "New topic"
    post("/testing/chat/set-topic", newTopic) {
      status should equal(200)
      topicShouldBe(newTopic)
    }
  }
}

