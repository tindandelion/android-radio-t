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

case class Topic(text: String, link: String = "") {
  def toJson: JObject = ("text" -> text) ~ ("link" -> link)
}

class TopicTrackerServlet(val root: String, val chatConfig: JabberConfig) extends ScalatraServlet
with AtmosphereSupport with SessionSupport
with JacksonJsonSupport with JValueResult {
  implicit protected val jsonFormats: Formats = DefaultFormats

  val logger = LoggerFactory.getLogger(getClass)
  val jabberChat = new JabberChat(chatConfig)
  var currentTopic = Topic("Default topic", "http://example.org")


  get("/") {
    "Hello world!"
  }

  atmosphere("/current-topic") {
    new AtmosphereClient {
      override def receive: AtmoReceive = {
        case TextMessage(text) => send(currentTopic.toJson)
      }
    }
  }

  post("/topic") {
    val values: Array[String] = request.body.split("\n")
    changeTopic(Topic(values(0), values(1)))
  }

  post("/heartbeat") {
    AtmosphereClient.broadcast(root + "/current-topic", " ")
  }

  def changeTopic(topic: Topic) {
    logger.info("Changing current topic to: [%s]".format(topic.text))

    currentTopic = topic
    AtmosphereClient.broadcast(root + "/current-topic", currentTopic.toJson)
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

object TopicTrackerServlet {
  val TopicStarter = "jc-radio-t"
}

