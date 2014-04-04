package org.dandelion.radiot.server


class TestableTopicTrackerServletTest extends RadiotServerSpec {
  override val servlet = new TestableTopicTrackerServlet("/testing/chat")
  addServlet(servlet, servlet.root + "/*")

  it("changes a topic by POST request") {
    val newTopic = "New topic"
    post("/testing/chat/set-topic", newTopic) {
      status should equal(200)
      topicShouldBe(newTopic)
    }
  }
}

