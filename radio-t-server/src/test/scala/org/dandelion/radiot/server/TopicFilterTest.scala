package org.dandelion.radiot.server

import org.scalatest.{Matchers, FunSpec}
import org.junit.runner.RunWith
import org.scalatest.junit.JUnitRunner

@RunWith(classOf[JUnitRunner])
class TopicFilterTest extends FunSpec with Matchers {
  val TopicId = "topic-id"
  val TopicStarter = "jc-radio-t"
  val TopicStarterJid = "online@conference/" + TopicStarter
  val InitialTopic = Topic("initial-topic-id", "Initial topic", "http://example.org")

  var topic: Topic = null
  val filter = new TopicFilter(TopicStarter, (newTopic) => topic = newTopic)

  it("suppresses messages which are not topic change notifications") {
    topic = InitialTopic
    filter(TopicId, TopicStarterJid, "Blah-blah from the robot")
    topic should equal(InitialTopic)
  }

  it("suppresses a message if the sender is not a topic starter") {
    topic = InitialTopic
    filter(TopicId, "online@conference/some-other-user", "--> New topic started")
    topic should equal(InitialTopic)
  }

  it("extracts the topic to the consumer when a sender is a topic starter") {
    topic = InitialTopic
    filter(TopicId, TopicStarterJid, "-->   New topic started   http://example.org  ")
    topic should equal(Topic(TopicId, "New topic started", "http://example.org"))
  }

}
