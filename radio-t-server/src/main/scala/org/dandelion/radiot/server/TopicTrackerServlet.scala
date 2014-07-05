package org.dandelion.radiot.server

import org.json4s.JsonAST.JObject
import org.json4s.JsonDSL._
import org.json4s.{DefaultFormats, Formats}
import org.scalatra.json.{JValueResult, JacksonJsonSupport}
import org.scalatra.{NoContent, Ok, ScalatraServlet}
import org.slf4j.LoggerFactory

case class Topic(text: String, link: String = "", id: String = "") {
  def toJson: JObject = ("id" -> id) ~ ("text" -> text) ~ ("link" -> link)
}

class TopicTrackerServlet(val root: String, val chatConfig: JabberConfig) extends ScalatraServlet
with JacksonJsonSupport with JValueResult {
  implicit protected val jsonFormats: Formats = DefaultFormats

  val logger = LoggerFactory.getLogger(getClass)
  val jabberChat = new JabberChat(chatConfig)
  var currentTopic: Option[Topic] = None


  get("/") {
    "Hello world!"
  }

  get("/topic") {
    contentType = formats("json")
    currentTopic match {
      case Some(topic) => Ok(topic)
      case None => NoContent()
    }
  }

  post("/topic") {
    val values: Array[String] = request.body.split("\n")
    changeTopic(Topic(values(0), values(1)))
  }

  def changeTopic(topic: Topic) {
    logger.info("Changing current topic to: [%s]".format(topic.text))
    currentTopic = Some(topic)
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

