package org.dandelion.radiot.server

import org.junit.runner.RunWith
import org.scalatest.junit.JUnitRunner
import org.scalatra.test.scalatest.ScalatraSpec
import org.scalatest.concurrent.Eventually

import scala.concurrent.duration._
import org.eclipse.jetty.websocket.client.WebSocketClient
import java.net.URI
import org.scalatest.{Matchers, BeforeAndAfter}

@RunWith(classOf[JUnitRunner])
abstract class RadiotServerSpec extends ScalatraSpec
with BeforeAndAfter with Eventually with Matchers {
  implicit val config = PatienceConfig(timeout = 5 seconds, interval = 0.5 seconds)

  val client = new WebSocketClient
  val socket = new TopicTrackerSocket

  def servlet: BaseTopicTrackerServlet

  before {
    client.start()
    client.connect(socket, serverUrl)
    eventually { socket should be('connected) }
  }

  after {
    client.stop()
  }

  def serverUrl = localPort match {
    case Some(port) => new URI(s"ws://localhost:$port${servlet.root}/current-topic")
    case None => throw new RuntimeException("No port is specified")
  }

  def topicShouldBe(expected: String) {
    eventually { socket.topic should equal(expected) }
  }
}
