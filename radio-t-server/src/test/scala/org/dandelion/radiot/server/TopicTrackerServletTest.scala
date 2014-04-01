package org.dandelion.radiot.server

import org.scalatest.{BeforeAndAfter, Matchers}
import org.eclipse.jetty.websocket.client.WebSocketClient
import java.net.URI
import org.eclipse.jetty.websocket.api.annotations._
import org.eclipse.jetty.websocket.api.Session
import org.scalatest.concurrent.Eventually

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

class TopicTrackerServletTest extends RadiotServerSpec
with Matchers with Eventually with BeforeAndAfter {

  addServlet(new TopicTrackerServlet, "/chat/*")

  val client = new WebSocketClient
  val socket = new TopicTrackerSocket

  before {
    client.start()
    client.connect(socket, serverUrl)
  }

  after {
    client.stop()
  }

  it("answers a simple request") {
    get("/chat") {
      status should equal(200)
      body should equal("Hello world!")
    }
  }

  it("receives current topic after connection is established") {
    eventually { socket should be('connected) }
    eventually { socket.topic should equal("Default topic") }
  }

  it("changes a topic by POST request") {
    eventually { socket should be('connected) }

    post("/chat/set-topic", "New topic") {
      status should equal(200)
      eventually { socket.topic should equal("New topic") }
    }
  }

  def serverUrl = localPort match {
    case Some(port) => new URI(s"ws://localhost:$port/chat/current-topic")
    case None => throw new RuntimeException("No port is specified")
  }
}
