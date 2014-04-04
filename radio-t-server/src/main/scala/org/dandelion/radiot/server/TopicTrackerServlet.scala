package org.dandelion.radiot.server

import org.scalatra.{SessionSupport, ScalatraServlet}
import org.scalatra.atmosphere._
import org.scalatra.json.{JValueResult, JacksonJsonSupport}
import org.json4s.{DefaultFormats, Formats}

import scala.concurrent._
import ExecutionContext.Implicits.global
import org.slf4j.LoggerFactory

class BaseTopicTrackerServlet(val root: String) extends ScalatraServlet
with AtmosphereSupport with SessionSupport
with JacksonJsonSupport with JValueResult {
  implicit protected val jsonFormats: Formats = DefaultFormats
  val logger = LoggerFactory.getLogger(getClass)

  private var currentTopic = "Default topic"

  atmosphere("/current-topic") {
    new AtmosphereClient {
      override def receive: AtmoReceive = {
        case TextMessage(text) => send(currentTopic)
      }
    }
  }

  def changeTopicTo(newTopic: String) {
    logger.info("Changing current topic to: [%s]".format(newTopic))

    currentTopic = newTopic
    AtmosphereClient.broadcast(root + "/current-topic", currentTopic)
  }
}

class TopicTrackerServlet(root: String) extends BaseTopicTrackerServlet(root) {
  val jabberChat = new JabberChat("android-radiot", "password") {
    override def onMessage(msg: String) = changeTopicTo(msg)
  }


  def connectToChat() {
    jabberChat.connect()
  }


  def disconnectFromChat() {
    jabberChat.disconnect()
  }

  get("/") {
    "Hello world!"
  }
}

class TestableTopicTrackerServlet(root: String) extends TopicTrackerServlet(root) {
  post("/set-topic") {
    changeTopicTo(request.body)
  }
}
