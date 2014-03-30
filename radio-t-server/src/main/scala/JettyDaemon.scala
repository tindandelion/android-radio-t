import org.apache.commons.daemon.{DaemonContext, Daemon}
import org.eclipse.jetty.server.Server
import org.eclipse.jetty.servlet.DefaultServlet
import org.eclipse.jetty.webapp.WebAppContext
import org.scalatra.servlet.ScalatraListener
import org.slf4j.LoggerFactory

class JettyDaemon extends Daemon {
  val Port = 8080
  val server = new Server(Port)
  val logger = LoggerFactory.getLogger(getClass)

  override def init(context: DaemonContext) {
    logger.info("Initializing daemon")
    val context = new WebAppContext()

    context setContextPath "/"
    context.setResourceBase("src/main/webapp")
    context.addEventListener(new ScalatraListener)
    context.addServlet(classOf[DefaultServlet], "/")
    server.setHandler(context)
  }

  override def start() {
    logger.info("Starting Jetty server")
    server.start()
  }

  override def stop() {
    logger.info("Stopping Jetty server")
    server.stop()
  }


  override def destroy() {
    logger.info("Daemon destroyed")
  }
}
