import org.scalatra.ScalatraServlet

class HelloWorldServlet extends ScalatraServlet {
  get("/") {
    "Hello world!"
  }
}
