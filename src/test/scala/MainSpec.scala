// test/Main.scala

package test

import org.scalatest._
import org.scalatest.flatspec.AnyFlatSpec
import org.scalatest.matchers.should.Matchers

class MainSpec extends AnyFlatSpec with Matchers {

  // Simple test that always passes
  "Main" should "always run tests" in {
    println("Running tests...")  // Print something just for fun
    val result = 1 == 1
    result should be(true)  // Assert that 1 == 1 is true
  }
}