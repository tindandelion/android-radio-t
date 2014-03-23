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

  val TopicRoute = "/current-topic"

  val logger = LoggerFactory.getLogger(getClass)
  var topic = "Default topic"



  get("/") {
    "Hello world!"
  }

  def changeTopicTo(newTopic: String) {
    logger.info("Changing current topic to: [%s]".format(newTopic))
    topic = newTopic
    AtmosphereClient.broadcast(routeBasePath + "/current-topic", topic)
  }

  post("/set-topic") {
    changeTopicTo(request.body)
  }

  atmosphere(TopicRoute) {
    new AtmosphereClient {
      override def receive: AtmoReceive = {
        case TextMessage(text) => send(topic)
      }
    }
  }

}
