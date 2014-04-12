package org.dandelion.radiot.server

import org.scalatest.{Matchers, FunSpec}
import org.junit.runner.RunWith
import org.scalatest.junit.JUnitRunner

@RunWith(classOf[JUnitRunner])
class TopicFilterTest extends FunSpec with Matchers {
  var topic = Topic("", "")
  val TopicStarter = "jc-radio-t"
  val filter = new TopicFilter(TopicStarter, (newTopic) => topic = newTopic)

  it("suppresses messages which are not topic change notifications") {
    topic = Topic("Current topic")
    filter("online@conference/" + TopicStarter, "Blah-blah from the robot")
    topic should equal(Topic("Current topic"))
  }

  it("extracts the topic to the consumer when a sender is a topic starter") {
    filter("online@conference/" + TopicStarter, "-->   New topic started   http://example.org  ")
    topic should equal(Topic("New topic started", "http://example.org"))
  }

  it("suppresses a message if the sender is not a topic starter") {
    topic = Topic("Current topic", "")
    filter("online@conference/some-other-user", "--> New topic started")
    topic should equal(Topic("Current topic"))
  }
}
