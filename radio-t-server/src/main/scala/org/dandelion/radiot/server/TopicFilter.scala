package org.dandelion.radiot.server

class TopicFilter(val topicStarter: String, val consumer: (Topic) => Unit)
  extends ((String, String) => Unit) {
  val ChatNickname = ".*/(.*)$".r
  val TopicChangeTemplate = "-->\\s+(.*)\\s+(http://.*)$".r

  override def apply(sender: String, message: String) = sender match {
    case ChatNickname(`topicStarter`) => filterTopicChangeNotification(message)
    case _ =>
  }

  def filterTopicChangeNotification(message: String) = message match {
    case TopicChangeTemplate(topic, link) => consumer(Topic(topic.trim, link.trim))
    case _ =>
  }
}
