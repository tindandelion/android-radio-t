package org.dandelion.radiot.server

import org.scalatest.Matchers
import org.scalatra.test.scalatest.ScalatraSpec
import org.eclipse.jetty.websocket.client.WebSocketClient
import java.net.URI
import org.eclipse.jetty.websocket.api.annotations._
import org.eclipse.jetty.websocket.api.Session
import org.scalatest.concurrent.Eventually
import scala.concurrent.duration._

@WebSocket
class SimpleEchoSocket {
  var session: Session = null
  var messages: List[String] = List()

  @OnWebSocketConnect
  def onConnect(session: Session) {
    this.session = session
  }

  @OnWebSocketMessage
  def onMessage(msg: String) {
    messages = msg :: messages
  }

  @OnWebSocketError
  def onError(error: Throwable) {
    println("Error " + error.toString)
  }

  def isConnected = (session != null) && session.isOpen

  def sendMessage(msg: String) = session.getRemote.sendString(msg)
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

  it("connects to a WebSocket server") {
    val client = new WebSocketClient
    val socket = new SimpleEchoSocket

    client.start()
    client.connect(socket, new URI("ws://echo.websocket.org"))
    eventually { socket should be('connected) }

    socket.sendMessage("Hello")
    socket.sendMessage("World")

    eventually { socket.messages should contain allOf("Hello", "World") }
    client.stop()
  }
}
