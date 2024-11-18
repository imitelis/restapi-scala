import bases.Meal
import io.circe.generic.auto._
import io.circe.parser.decode
import io.circe.syntax._
import org.scalatest.flatspec.AnyFlatSpec
import org.scalatest.matchers.should.Matchers

import java.util.UUID

class MealSpec extends AnyFlatSpec with Matchers {

  "Meal" should "correctly serialize to JSON" in {
    val meal = Meal(UUID.randomUUID(), "Pizza", 2, List("Cheese", "Tomato"))
    
    val json = meal.asJson.noSpaces
    
    // Check if the JSON string contains the correct fields
    json.should(include("Pizza"))
    json.should(include("Cheese"))
    json.should(include("Tomato"))
  }

  it should "correctly deserialize from JSON" in {
    val mealJson = """{
                      |  "id": "f47ac10b-58cc-4372-a567-0e02b2c3d479",
                      |  "name": "Pizza",
                      |  "servings": 2,
                      |  "ingredients": ["Cheese", "Tomato"]
                      |}""".stripMargin

    val uuid = UUID.fromString("f47ac10b-58cc-4372-a567-0e02b2c3d479")
    val expectedMeal = Meal(uuid, "Pizza", 2, List("Cheese", "Tomato"))
    
    // Decode the JSON string into a Meal object
    val decodedMeal = decode[Meal](mealJson)
    
    decodedMeal.should(be(Right(expectedMeal)))
  }

  it should "be equal to another Meal with the same data" in {
    val meal1 = Meal(UUID.randomUUID(), "Pizza", 2, List("Cheese", "Tomato"))
    val meal2 = meal1.copy()  // Create a copy of meal1
    
    meal1.shouldEqual(meal2)  // Case classes automatically provide equality checking
  }

  it should "return a meaningful string representation" in {
    val meal = Meal(UUID.randomUUID(), "Pizza", 2, List("Cheese", "Tomato"))
    
    meal.toString.should(include("Pizza"))
    meal.toString.should(include("Cheese"))
    meal.toString.should(include("Tomato"))
  }

  it should "copy a meal with new values" in {
    val meal = Meal(UUID.randomUUID(), "Pizza", 2, List("Cheese", "Tomato"))
    
    val copiedMeal = meal.copy(name = "Pasta", servings = 3)
    
    copiedMeal.name.should(be("Pasta"))
    copiedMeal.servings.should(be(3))
    copiedMeal.ingredients.should(be(meal.ingredients)) // ingredients should stay the same
  }
}
