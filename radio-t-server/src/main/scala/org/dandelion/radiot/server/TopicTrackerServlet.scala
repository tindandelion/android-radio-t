package org.dandelion.radiot.server

import org.scalatra.{SessionSupport, ScalatraServlet}
import org.scalatra.atmosphere._
import org.scalatra.json.{JValueResult, JacksonJsonSupport}
import org.json4s.{DefaultFormats, Formats}

import scala.concurrent._
import ExecutionContext.Implicits.global
import org.slf4j.LoggerFactory
import org.jivesoftware.smack.{PacketListener, XMPPConnection, ConnectionConfiguration}
import org.jivesoftware.smackx.muc.MultiUserChat
import org.jivesoftware.smack.packet.{Message, Packet}

class TopicTrackerServlet extends ScalatraServlet
with AtmosphereSupport with SessionSupport
with JacksonJsonSupport with JValueResult {

  implicit protected val jsonFormats: Formats = DefaultFormats

  val logger = LoggerFactory.getLogger(getClass)
  var currentTopic = "Default topic"

  val topicListener: PacketListener = new PacketListener {
    override def processPacket(packet: Packet) {
      val msg: Message = packet.asInstanceOf[Message]
      changeTopicTo(msg.getBody)
    }
  }

  val connection = new XMPPConnection(new ConnectionConfiguration("localhost", 5222))
  var chat: MultiUserChat = null

  def connectToChat() {
    connection.connect()
    connection.login("android-radiot", "password")
    chat = new MultiUserChat(connection, "online@conference.precise32")
    chat.join("android-radiot")
    chat.addMessageListener(topicListener)
  }

  def disconnectFromChat() {
    chat.leave()
    connection.disconnect()
  }

  get("/") {
    "Hello world!"
  }

  post("/set-topic") {
    changeTopicTo(request.body)
  }

  atmosphere("/current-topic") {
    new AtmosphereClient {
      override def receive: AtmoReceive = {
        case TextMessage(text) => send(currentTopic)
      }
    }
  }


  def changeTopicTo(newTopic: String) {
    logger.info("Changing current topic to: [%s]".format(newTopic))
    currentTopic = newTopic
    notifyClients()
  }

  def notifyClients() {
    if (request != null)
      AtmosphereClient.broadcast(routeBasePath + "/current-topic", currentTopic)
  }
}
