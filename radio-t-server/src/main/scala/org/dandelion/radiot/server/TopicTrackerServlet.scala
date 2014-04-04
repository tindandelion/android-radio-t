package org.dandelion.radiot.server

import org.scalatra.{SessionSupport, ScalatraServlet}
import org.scalatra.atmosphere._
import org.scalatra.json.{JValueResult, JacksonJsonSupport}
import org.json4s.{DefaultFormats, Formats}

import scala.concurrent._
import ExecutionContext.Implicits.global
import org.slf4j.LoggerFactory

class TopicTrackerServlet extends ScalatraServlet
with AtmosphereSupport with SessionSupport
with JacksonJsonSupport with JValueResult {

  implicit protected val jsonFormats: Formats = DefaultFormats

  val logger = LoggerFactory.getLogger(getClass)
  val jabberChat = new JabberChat("android-radiot", "password") {
    override def onMessage(msg: String) = changeTopicTo(msg)
  }

  var currentTopic = "Default topic"


  def connectToChat() {
    jabberChat.connect()
  }

  def disconnectFromChat() {
    jabberChat.disconnect()
  }

  get("/") {
    "Hello world!"
  }

  post("/set-topic") {
    changeTopicTo(request.body)
  }

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
    AtmosphereClient.broadcastAll(currentTopic)
  }
}
