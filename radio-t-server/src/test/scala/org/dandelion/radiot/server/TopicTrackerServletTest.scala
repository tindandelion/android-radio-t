package org.dandelion.radiot.server

import org.scalatest.Matchers
import org.scalatra.test.scalatest.ScalatraSpec
import org.eclipse.jetty.websocket.client.WebSocketClient
import java.net.URI
import org.eclipse.jetty.websocket.api.annotations._
import org.eclipse.jetty.websocket.api.Session
import org.scalatest.concurrent.Eventually
import scala.concurrent.duration._
import org.eclipse.jetty.websocket.api.extensions.Frame

@WebSocket
class TopicTrackerSocket {
  var topic: String = ""
  private var session: Session = null

  @OnWebSocketConnect
  def onConnect(session: Session) {
    this.session = session
    session.getRemote.sendString("get")
  }

  @OnWebSocketMessage
  def onMessage(msg: String) {
    topic = msg
  }

  def isConnected = (session != null) && session.isOpen
}

class TopicTrackerServletTest extends ScalatraSpec with Matchers with Eventually {
  implicit override def patienceConfig = PatienceConfig(timeout = 5 seconds)

  addServlet(new TopicTrackerServlet, "/*")

  it("answers a simple request") {
    get("/") {
      status should equal(200)
      body should equal("Hello world!")
    }
  }

  it("receives current topic after connection is established") {
    val client = new WebSocketClient
    val socket = new TopicTrackerSocket

    client.start()
    client.connect(socket, serverUrl)

    eventually { socket should be('connected) }
    eventually { socket.topic should equal("Default topic") }

    client.stop()
  }

  def serverUrl = localPort match {
    case Some(port) => new URI(s"ws://localhost:$port/current-topic")
    case None => throw new RuntimeException("No port is specified")
  }
}
