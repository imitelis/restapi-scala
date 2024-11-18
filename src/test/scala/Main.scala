// test/Main.scala

package test

import org.scalatest._
import org.scalatest.flatspec.AnyFlatSpec
import org.scalatest.matchers.should.Matchers

class Main extends AnyFlatSpec with Matchers {

  // Simple test that always passes
  "1 == 1" should "always be true" in {
    println("Running tests...")  // Print something just for fun
    val result = 1 == 1
    result should be(true)  // Assert that 1 == 1 is true
  }
}