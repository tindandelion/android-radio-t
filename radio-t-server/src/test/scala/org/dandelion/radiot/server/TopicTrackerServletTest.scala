package org.dandelion.radiot.server

import org.json4s.DefaultFormats
import org.json4s.jackson.JsonMethods._
import org.junit.runner.RunWith
import org.scalatest.Matchers
import org.scalatest.junit.JUnitRunner
import org.scalatest.matchers.{MatchResult, Matcher}
import org.scalatra.test.ClientResponse
import org.scalatra.test.scalatest.ScalatraSpec

import scala.concurrent.duration._


@RunWith(classOf[JUnitRunner])
class TopicTrackerServletTest extends ScalatraSpec with Matchers {
  implicit val formats = DefaultFormats

  val LocalChatConfig = JabberConfig(
    server = "localhost",
    username = "android-radiot",
    password = "password",
    room = "online@conference.precise64")

  val LocalAdminConfig = LocalChatConfig.copy(username = TopicTrackerServlet.TopicStarter)
  var currentClock = 0.milliseconds

  val haveNoContent = Matcher { (r: ClientResponse) =>
    MatchResult(r.status == 204,
      "did not return code 204 (No Content)",
      "did return code 204 (No Content)")
  } and Matcher { (r: ClientResponse) =>
    MatchResult(r.body.isEmpty,
      "body was not empty",
      "body was empty")
  }

  val servlet = new TopicTrackerServlet(LocalChatConfig) {
    override def clock = currentClock
  }

  addServlet(servlet, "/chat/*")

  it("answers a simple request") {
    get("/chat") {
      status should equal(200)
      body should equal("Hello world!")
    }
  }

  it("answers no data while no current topic is set") {
    get("/chat/topic") {
      response should haveNoContent
    }
  }

  it("answers a topic JSON when current topic is set") {
    sendMessageToChat(s"--> New topic to discuss http://new-topic.org")

    get("/chat/topic") {
      status should equal(200)
      header("Content-Type") should equal("application/json; charset=UTF-8")

      val receivedTopic = extractTopic(body)
      receivedTopic.id shouldNot be('empty)
      receivedTopic.text should equal("New topic to discuss")
      receivedTopic.link should equal("http://new-topic.org")
    }
  }


  it("expires the topic after certain time") {
    sendMessageToChat(s"--> New topic to discuss http://new-topic.org")
    millisecondsPass(TopicTrackerServlet.TopicExpirationTimeout)

    get("/chat/topic") {
      response should haveNoContent
    }
  }

  def extractTopic(json: String): Topic = parse(json).extract[Topic]


  def millisecondsPass(timeout: FiniteDuration) = {
    currentClock = currentClock + timeout
  }

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



