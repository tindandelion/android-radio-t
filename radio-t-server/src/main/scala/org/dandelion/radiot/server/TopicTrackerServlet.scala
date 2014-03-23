package org.dandelion.radiot.server

import org.scalatra.{SessionSupport, ScalatraServlet}
import org.scalatra.atmosphere._
import org.scalatra.json.{JValueResult, JacksonJsonSupport}
import org.json4s.{DefaultFormats, Formats}

import scala.concurrent._
import ExecutionContext.Implicits.global

class TopicTrackerServlet extends ScalatraServlet
with AtmosphereSupport with SessionSupport
with JacksonJsonSupport with JValueResult {

  implicit protected val jsonFormats: Formats = DefaultFormats

  val TopicRoute = "/current-topic"

  var topic = "Default topic"

  get("/") {
    "Hello world!"
  }

  def changeTopicTo(newTopic: String) {
    topic = newTopic
    AtmosphereClient.broadcast(TopicRoute, topic)
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
