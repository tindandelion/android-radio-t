import org.eclipse.jetty.server.handler.HandlerCollection
import org.eclipse.jetty.server.{Handler, Server}
import org.eclipse.jetty.servlet.DefaultServlet
import org.eclipse.jetty.webapp.WebAppContext
import org.scalatra.servlet.ScalatraListener
import org.slf4j.LoggerFactory

object ServerStarter extends App {
  val Port = 80
  val server = new Server(Port)
  val logger = LoggerFactory.getLogger(getClass)

  logger.info("Initializing Jetty")
  server.setHandler(handlerCollectionOf(createContext))

  logger.info("Starting Jetty")
  server.start()

  private def createContext: WebAppContext = {
    val context = new WebAppContext()

    context setContextPath "/"
    context.setResourceBase("src/main/webapp")
    context.addEventListener(new ScalatraListener)
    context.addServlet(classOf[DefaultServlet], "/")
    context
  }

  def handlerCollectionOf(handlers: Handler*): Handler = {
    val collection = new HandlerCollection
    for (h <- handlers) collection.addHandler(h)
    collection
  }
}