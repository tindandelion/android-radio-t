import org.dandelion.radiot.server.{TestableTopicTrackerServlet, TopicTrackerServlet}
import org.scalatra._
import javax.servlet.ServletContext

class ScalatraBootstrap extends LifeCycle {
  override def init(context: ServletContext) {
    context.mount(new TopicTrackerServlet("/chat"), "/chat/*")
    context.mount(new TestableTopicTrackerServlet("/testing/chat"), "/testing/chat/*")
  }
}
