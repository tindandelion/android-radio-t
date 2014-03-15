package org.dandelion.radiot.server

import org.scalatra.ScalatraServlet

class HelloWorld extends ScalatraServlet {
  get("/") {
    "Hello world!"
  }
}
