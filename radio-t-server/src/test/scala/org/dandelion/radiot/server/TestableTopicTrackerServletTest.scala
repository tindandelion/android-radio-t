package org.dandelion.radiot.server


class TestableTopicTrackerServletTest extends RadiotServerSpec {
  override val servlet = new TestableTopicTrackerServlet("/testing/chat")
  addServlet(servlet, servlet.root + "/*")

  it("changes a topic by POST request") {
    val text = "New topic"
    val link = "http://example.org"
    val requestBody = text + "\n" + link
    post("/testing/chat/set-topic", requestBody) {
      status should equal(200)
      topicShouldBe(text, link)
    }
  }
}

