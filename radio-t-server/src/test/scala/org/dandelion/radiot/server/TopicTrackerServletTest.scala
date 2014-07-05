package org.dandelion.radiot.server

import org.json4s.DefaultFormats
import org.json4s.jackson.JsonMethods._
import org.junit.runner.RunWith
import org.scalatest.Matchers
import org.scalatest.junit.JUnitRunner
import org.scalatra.test.scalatest.ScalatraSpec


@RunWith(classOf[JUnitRunner])
class TopicTrackerServletTest extends ScalatraSpec with Matchers {
  implicit val formats = DefaultFormats

  val LocalChatConfig = JabberConfig(
    server = "localhost",
    username = "android-radiot",
    password = "password",
    room = "online@conference.precise64")

  val LocalAdminConfig = LocalChatConfig.copy(username = TopicTrackerServlet.TopicStarter)

  val servlet = new TopicTrackerServlet("/chat", LocalChatConfig)
  addServlet(servlet, servlet.root + "/*")

  it("answers a simple request") {
    get("/chat") {
      status should equal(200)
      body should equal("Hello world!")
    }
  }

  it("answers no data while no current topic is set") {
    get("/chat/topic") {
      status should equal(204)
      body should be('empty)
    }
  }

  it("answers a topic JSON when current topic is set") {
    val newTopic = Topic("New topic to discuss", "http://new-topic.org")
    sendMessageToChat(s"--> ${newTopic.text} ${newTopic.link}")

    get("/chat/topic") {
      status should equal(200)
      header("Content-Type") should equal("application/json; charset=UTF-8")

      val receivedTopic = extractTopic(body)
      receivedTopic.id shouldNot be('empty)
      receivedTopic.text should equal(newTopic.text)
      receivedTopic.link should equal(newTopic.link)
    }

  }

  def extractTopic(json: String): Topic = parse(json).extract[Topic]

  def sendMessageToChat(msg: String) {
    new JabberChat(LocalAdminConfig) {
      connect {
        (_, _, _) =>
      }
      sendMessage(msg)
      disconnect()
    }
  }
}



