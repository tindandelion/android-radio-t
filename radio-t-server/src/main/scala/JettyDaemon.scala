import org.apache.commons.daemon.{DaemonContext, Daemon}
import org.eclipse.jetty.server.Server
import org.eclipse.jetty.servlet.DefaultServlet
import org.eclipse.jetty.webapp.WebAppContext
import org.scalatra.servlet.ScalatraListener
import org.slf4j.LoggerFactory

trait JettyStarter {
  val Port = 8080
  val server = new Server(Port)
  val logger = LoggerFactory.getLogger(getClass)

  def initJetty() {
    logger.info("Initializing daemon")
    val context = new WebAppContext()

    context setContextPath "/"
    context.setResourceBase("src/main/webapp")
    context.addEventListener(new ScalatraListener)
    context.addServlet(classOf[DefaultServlet], "/")
    server.setHandler(context)
  }

  def startJetty(): Unit = {
    logger.info("Starting Jetty server")
    server.start()
  }

  def stopJetty(): Unit = {
    logger.info("Stopping Jetty server")
    server.stop()
  }
}

class JettyDaemon extends Daemon with JettyStarter {

  override def init(context: DaemonContext): Unit = {
    initJetty()
  }

  override def start() {
    startJetty()
  }

  override def stop() {
    stopJetty()
  }


  override def destroy() {
    logger.info("Daemon destroyed")
  }
}

object ServerCommandLine extends App with JettyStarter {
  initJetty()
  startJetty()
}