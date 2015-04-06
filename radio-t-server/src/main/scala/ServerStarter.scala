import org.apache.commons.daemon.{DaemonContext, Daemon}
import org.eclipse.jetty.server.Server
import org.eclipse.jetty.servlet.DefaultServlet
import org.eclipse.jetty.webapp.WebAppContext
import org.scalatra.servlet.ScalatraListener
import org.slf4j.LoggerFactory

object ServerStarter extends App {
  val Port = 80
  val server = new Server(Port)
  val logger = LoggerFactory.getLogger(getClass)

  logger.info("Initializing Jetty")
  val context = new WebAppContext()

  context setContextPath "/"
  context.setResourceBase("src/main/webapp")
  context.addEventListener(new ScalatraListener)
  context.addServlet(classOf[DefaultServlet], "/")
  server.setHandler(context)

  logger.info("Starting Jetty")
  server.start()
}