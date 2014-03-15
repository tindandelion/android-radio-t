import org.dandelion.radiot.server.HelloWorld
import org.scalatest.Matchers
import org.scalatra.test.scalatest.ScalatraSpec

class HelloWorldTest extends ScalatraSpec with Matchers {
  addServlet(new HelloWorld, "/*")

  it("fetches the root") {
    get("/") {
      status should equal(200)
      body should equal("Hello world!")
    }
  }
}
