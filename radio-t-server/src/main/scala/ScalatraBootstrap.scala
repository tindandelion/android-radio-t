import org.dandelion.radiot.server.TopicTrackerServlet
import org.scalatra._
import javax.servlet.ServletContext

class ScalatraBootstrap extends LifeCycle {
  override def init(context: ServletContext) {
    context.mount(new TopicTrackerServlet, "/chat/*")
  }
}
