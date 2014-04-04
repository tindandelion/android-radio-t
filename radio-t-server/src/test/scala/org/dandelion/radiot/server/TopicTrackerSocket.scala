package org.dandelion.radiot.server

import org.eclipse.jetty.websocket.api.annotations.{OnWebSocketMessage, OnWebSocketConnect, WebSocket}
import org.eclipse.jetty.websocket.api.Session

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
