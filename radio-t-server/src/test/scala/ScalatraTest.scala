import org.scalatest.Matchers
import org.scalatra.test.scalatest.ScalatraSpec

class ScalatraTest extends ScalatraSpec with Matchers {
  addServlet(classOf[HelloWorldServlet], "/*")

  it("fetches the root") {
    get("/") {
      status should equal(200)
      body should equal("Hello world!")
    }
  }
}
