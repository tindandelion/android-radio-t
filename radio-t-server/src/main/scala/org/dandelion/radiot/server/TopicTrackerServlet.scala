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

  get("/") {
    "Hello world!"
  }

  atmosphere("/current-topic") {
    new AtmosphereClient {
      override def receive: AtmoReceive = {
        case Connected =>
          println("Connected a client!")
          send("Hello")
          send("World")
          println("Messages sent")
      }
    }
  }

}
