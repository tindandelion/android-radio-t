package org.dandelion.radiot.server

import org.json4s.JsonAST.JObject
import org.json4s.JsonDSL._
import org.json4s.{DefaultFormats, Formats}
import org.scalatra.json.{JValueResult, JacksonJsonSupport}
import org.scalatra.{NoContent, Ok, ScalatraServlet}
import org.slf4j.LoggerFactory

import scala.concurrent.duration._

case class Topic(id: String, text: String, link: String) {
  def toJson: JObject = ("id" -> id) ~ ("text" -> text) ~ ("link" -> link)
}

class TopicTrackerServlet(val chatConfig: JabberConfig)
  extends ScalatraServlet
  with JacksonJsonSupport with JValueResult {
  implicit protected val jsonFormats: Formats = DefaultFormats

  val logger = LoggerFactory.getLogger(getClass)
  val jabberChat = new JabberChat(chatConfig)
  var currentTopic: Option[(Topic, FiniteDuration)] = None


  get("/") {
    "Hello world!"
  }

  get("/topic") {
    contentType = formats("json")
    currentTopic match {
      case Some((topic, timestamp)) =>
        if (isActual(timestamp)) Ok(topic) else NoContent()
      case None => NoContent()
    }
  }

  def isActual(timestamp: FiniteDuration) =
    (clock - timestamp) < TopicTrackerServlet.TopicExpirationTimeout

  def changeTopic(topic: Topic) {
    logger.info("Changing current topic to: [%s][%s]".format(topic.id, topic.text))
    currentTopic = Some((topic, clock))
  }

  def clock = System.currentTimeMillis().milliseconds

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
  val TopicExpirationTimeout = 60.minutes
}

