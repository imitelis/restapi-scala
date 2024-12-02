import bases._

import io.circe.generic.auto._
import io.circe.parser.decode
import io.circe.syntax._

import org.scalatest.flatspec.AnyFlatSpec
import org.scalatest.matchers.should.Matchers

import java.util.UUID

class GreetingSpec extends AnyFlatSpec with Matchers {

  "Greeting" should "correctly serialize to JSON" in {
    val greeting = Greeting("Hello, world!")
    
    val json = greeting.asJson.noSpaces
    
    // Check if the JSON string contains the correct fields
    json should include("Hello, world!")
  }

  it should "correctly deserialize from JSON" in {
    val greetingJson = """{
                          |  "message": "Hello, world!"
                          |}""".stripMargin

    val expectedGreeting = Greeting("Hello, world!")
    
    // Decode the JSON string into a Greeting object
    val decodedGreeting = decode[Greeting](greetingJson)
    
    decodedGreeting should be(Right(expectedGreeting))
  }

  it should "be equal to another Greeting with the same data" in {
    val greeting1 = Greeting("Hello, world!")
    val greeting2 = greeting1.copy()  // Create a copy of greeting1
    
    greeting1 shouldEqual greeting2  // Case classes automatically provide equality checking
  }

  it should "return a meaningful string representation" in {
    val greeting = Greeting("Hello, world!")
    
    greeting.toString should include("Hello, world!")
  }

  it should "copy a Greeting with new values" in {
    val greeting = Greeting("Hello, world!")
    
    val copiedGreeting = greeting.copy(message = "Goodbye, world!")
    
    copiedGreeting.message should be("Goodbye, world!")
  }
}