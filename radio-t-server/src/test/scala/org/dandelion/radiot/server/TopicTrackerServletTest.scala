package org.dandelion.radiot.server

import org.scalatest.Matchers
import org.scalatra.test.scalatest.ScalatraSpec

class TopicTrackerServletTest extends ScalatraSpec with Matchers {
  addServlet(new TopicTrackerServlet, "/*")

  it("answers a simple request") {
    get("/") {
      status should equal(200)
      body should equal("Hello world!")
    }
  }
}
