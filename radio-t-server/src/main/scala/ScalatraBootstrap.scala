import java.io.{FileInputStream, File}
import java.util.Properties
import org.dandelion.radiot.server.{JabberConfig, TopicTrackerServlet}
import org.scalatra._
import javax.servlet.ServletContext

class ScalatraBootstrap extends LifeCycle {
  val ConfigFile = new File("/etc/radio-t-server.conf")

  override def init(context: ServletContext) {
    context.mount(new TopicTrackerServlet("/chat", loadChatConfig(ConfigFile)), "/chat/*")
  }


  def loadChatConfig(file: File) = {
    val props =
      if (file.exists()) readPropertiesFrom(file)
      else System.getProperties

    configFrom(props)
  }

  def readPropertiesFrom(file: File) = {
    val props = new Properties()
    props.load(new FileInputStream(file))
    props
  }

  def configFrom(props: Properties) = {
    JabberConfig(
      server = props.getProperty("xmpp.server"),
      username = props.getProperty("xmpp.username"),
      password = props.getProperty("xmpp.password"),
      room = props.getProperty("xmpp.room"))
  }
}
