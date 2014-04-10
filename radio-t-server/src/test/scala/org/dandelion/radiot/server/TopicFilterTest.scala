package org.dandelion.radiot.server

import org.scalatest.{Matchers, FunSpec}
import org.junit.runner.RunWith
import org.scalatest.junit.JUnitRunner

@RunWith(classOf[JUnitRunner])
class TopicFilterTest extends FunSpec with Matchers {
  var topic = ""
  val TopicStarter = "jc-radio-t"
  val filter = new TopicFilter(TopicStarter, msg => topic = msg)

  it("suppresses messages which are not topic change notifications") {
    topic = "Current topic"
    filter("online@conference/" + TopicStarter, "Blah-blah from the robot")
    topic should equal("Current topic")
  }

  it("extracts the topic to the consumer when a sender is a topic starter") {
    filter("online@conference/" + TopicStarter, "-->   New topic started")
    topic should equal("New topic started")
  }

  it("suppresses a message if the sender is not a topic starter") {
    topic = "Current topic"
    filter("online@conference/some-other-user", "--> New topic started")
    topic should equal("Current topic")
  }
}
