package org.dandelion.radiot.server

class TopicFilter(val topicStarter: String, val consumer: (String) => Unit)
  extends ((String, String) => Unit) {
  val ChatNickname = ".*/(.*)$".r
  val TopicChangeTemplate = "-->\\s+(.*)".r

  override def apply(sender: String, message: String) = sender match {
    case ChatNickname(`topicStarter`) => filterTopicChangeNotification(message)
    case _ =>
  }

  def filterTopicChangeNotification(message: String) = message match {
    case TopicChangeTemplate(topic) => consumer(topic)
    case _ =>
  }
}
