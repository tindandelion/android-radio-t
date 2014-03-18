package org.dandelion.radiot.server

import org.scalatra.ScalatraServlet

class TopicTrackerServlet extends ScalatraServlet {
  get("/") { "Hello world!" }
}
