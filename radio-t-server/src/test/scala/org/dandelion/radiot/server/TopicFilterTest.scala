package org.dandelion.radiot.server

import org.scalatest.{Matchers, FunSpec}

class TopicFilterTest extends FunSpec with Matchers {
  it("passes a message to the consumer when a sender is a topic starter") {
    var topic = ""
    val topicStarter = "jc-radio-t"
    val filter = new TopicFilter(topicStarter, msg => topic = msg)

    filter("online@conference/" + topicStarter, "New topic started")
    topic should equal("New topic started")
  }

  it("suppresses a message if the sender is not a topic starter") {
    var topic = "Current topic"
    val topicStarter = "jc-radio-t"
    val filter = new TopicFilter(topicStarter, msg => topic = msg)

    filter("online@conference/some-other-user", "New topic started")
    topic should equal("Current topic")
  }
}
