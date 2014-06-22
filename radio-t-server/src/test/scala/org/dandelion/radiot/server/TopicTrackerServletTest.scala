package org.dandelion.radiot.server

import java.net.URI
import org.eclipse.jetty.websocket.client.WebSocketClient
import scala.concurrent.duration._
import org.scalatra.test.scalatest.ScalatraSpec
import org.scalatest.{Matchers, BeforeAndAfter}
import org.scalatest.concurrent.Eventually
import org.junit.runner.RunWith
import org.scalatest.junit.JUnitRunner

import org.json4s.jackson.JsonMethods._


@RunWith(classOf[JUnitRunner])
class TopicTrackerServletTest extends ScalatraSpec
with BeforeAndAfter with Eventually with Matchers {
  implicit val config = PatienceConfig(timeout = 5 seconds, interval = 0.5 seconds)

  val LocalChatConfig = JabberConfig(
    server = "localhost",
    username = "android-radiot",
    password = "password",
    room = "online@conference.precise64")

  val LocalAdminConfig = LocalChatConfig.copy(username = TopicTrackerServlet.TopicStarter)

  val client = new WebSocketClient
  val socket = new TopicTrackerSocket
  val servlet = new TopicTrackerServlet("/chat", LocalChatConfig)

  addServlet(servlet, servlet.root + "/*")

  before {
    client.start()
    client.connect(socket, serverUrl)
    eventually { socket should be('connected) }
  }

  after {
    client.stop()
  }

  it("answers a simple request") {
    get("/chat") {
      status should equal(200)
      body should equal("Hello world!")
    }
  }

  it("answers no data while no current topic is set") {
    get("/chat/topic") {
      status should equal(204)
      body shouldBe 'empty
    }
  }

  it("answers a topic JSON when current topic is set") {
    val newTopic = Topic("New topic to discuss", "http://new-topic.org")
    sendMessageToChat(s"--> ${newTopic.text} ${newTopic.link}")

    get("/chat/topic") {
      status should equal(200)
      header("Content-Type") should equal("application/json; charset=UTF-8")
      body should equal(topicJson(newTopic))
    }

  }

  it("receives current topic after connection is established") {
    topicShouldBe("Default topic", "http://example.org")
  }

  it("changes a topic by a message from the chat") {
    val newTopic = Topic("New topic to discuss", "http://new-topic.org")
    sendMessageToChat(s"--> ${newTopic.text} ${newTopic.link}")
    topicShouldBe(newTopic.text, newTopic.link)
  }

  it("changes a topic by POST request") {
    val text = "New topic"
    val link = "http://example.org"
    val requestBody = text + "\n" + link
    post("/chat/topic", requestBody) {
      status should equal(200)
      topicShouldBe(text, link)
    }
  }

  def serverUrl = localPort match {
    case Some(port) => new URI(s"ws://localhost:$port${servlet.root}/current-topic")
    case None => throw new RuntimeException("No port is specified")
  }

  def sendMessageToChat(msg: String) {
    new JabberChat(LocalAdminConfig) {
      connect {
        (_, _) =>
      }
      sendMessage(msg)
      disconnect()
    }
  }

  def topicJson(newTopic: Topic): String = {
    compact(render(newTopic.toJson))
  }

  def topicShouldBe(text: String, link: String) =
    eventually { socket.topic should equal(Topic(text, link)) }
}



