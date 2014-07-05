package org.dandelion.radiot.server

class TopicFilter(val topicStarter: String, val consumer: (Topic) => Unit)
  extends ((String, String, String) => Unit) {
  val ChatNickname = ".*/(.*)$".r
  val TopicChangeTemplate = "-->\\s+(.*)\\s+(http://.*)$".r

  override def apply(id: String, sender: String, message: String) = sender match {
    case ChatNickname(`topicStarter`) => filterTopicChangeNotification(id, message)
    case _ =>
  }

  def filterTopicChangeNotification(id: String, message: String) = message match {
    case TopicChangeTemplate(topic, link) => consumer(Topic(id, topic.trim, link.trim))
    case _ =>
  }
}
