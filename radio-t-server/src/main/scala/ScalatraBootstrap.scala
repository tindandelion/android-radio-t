import java.io.{FileInputStream, File}
import java.util.Properties
import org.dandelion.radiot.server.{JabberConfig, TopicTrackerServlet}
import org.scalatra._
import javax.servlet.ServletContext

class ScalatraBootstrap extends LifeCycle {
  val ConfigFile = new File("/etc/radio-t-server.conf")

  override def init(context: ServletContext) {
    context.mount(new TopicTrackerServlet(loadChatConfig(ConfigFile)), "/api/chat/v1/*")
  }

  def loadChatConfig(file: File) =
    if (!file.exists()) throw new RuntimeException(s"Configuration file [$file] not found")
    else readProperties(file)

  def readProperties(file: File) = {
    val props = new Properties()
    props.load(new FileInputStream(file))

    JabberConfig(
      server = props.getProperty("xmpp.server"),
      username = props.getProperty("xmpp.username"),
      password = props.getProperty("xmpp.password"),
      room = props.getProperty("xmpp.room"))
  }
}
