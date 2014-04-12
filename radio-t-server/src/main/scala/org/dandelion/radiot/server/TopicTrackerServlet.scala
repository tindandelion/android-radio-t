package org.dandelion.radiot.server

import org.scalatra.{SessionSupport, ScalatraServlet}
import org.scalatra.atmosphere._
import org.scalatra.json.{JValueResult, JacksonJsonSupport}
import org.json4s.{DefaultFormats, Formats}
import org.json4s.JsonDSL._

import scala.concurrent._
import ExecutionContext.Implicits.global
import org.slf4j.LoggerFactory
import org.json4s.JsonAST.JObject

class BaseTopicTrackerServlet(val root: String) extends ScalatraServlet
with AtmosphereSupport with SessionSupport
with JacksonJsonSupport with JValueResult {
  implicit protected val jsonFormats: Formats = DefaultFormats
  val logger = LoggerFactory.getLogger(getClass)

  private var currentTopic = "Default topic"

  atmosphere("/current-topic") {
    new AtmosphereClient {
      override def receive: AtmoReceive = {
        case TextMessage(text) => send(toJson(currentTopic))
      }
    }
  }

  def changeTopic(newTopic: String) {
    logger.info("Changing current topic to: [%s]".format(newTopic))

    currentTopic = newTopic
    AtmosphereClient.broadcast(root + "/current-topic", toJson(currentTopic))
  }

  def toJson(topic: String): JObject = {
    "topic" -> topic
  }
}

object TopicTrackerServlet {
  val TopicStarter = "jc-radio-t"
}

class TopicTrackerServlet(root: String, val chatConfig: JabberConfig) extends BaseTopicTrackerServlet(root) {
  val jabberChat = new JabberChat(chatConfig)

  get("/") {
    "Hello world!"
  }


  override def init() {
    super.init()
    jabberChat.connect(new TopicFilter(TopicTrackerServlet.TopicStarter, changeTopic))
  }

  override def destroy() {
    jabberChat.disconnect()
    super.destroy()
  }
}

class TestableTopicTrackerServlet(root: String) extends BaseTopicTrackerServlet(root) {
  post("/set-topic") {
    changeTopic(request.body)
  }

  post("/heartbeat") {
    AtmosphereClient.broadcast(root + "/current-topic", " ")
  }
}

