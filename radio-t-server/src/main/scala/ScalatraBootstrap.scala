import java.io.File
import org.dandelion.radiot.server.{JabberConfig, TestableTopicTrackerServlet, TopicTrackerServlet}
import org.scalatra._
import javax.servlet.ServletContext

class ScalatraBootstrap extends LifeCycle {
  val ConfigFile = new File("/etc/radio-t-server.conf")

  override def init(context: ServletContext) {
    context.mount(new TopicTrackerServlet("/chat", loadChatConfig(ConfigFile)), "/chat/*")
    context.mount(new TestableTopicTrackerServlet("/testing/chat"), "/testing/chat/*")
  }

  def loadChatConfig(file: File) =
    if (!file.exists()) throw new RuntimeException("Config file not found")
    else JabberConfig("localhost", "android-radiot", "password", "online@conference.precise64")
}
