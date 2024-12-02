import bases._
import models._
import services._
import repositories._

import org.scalatest._
import org.scalatest.flatspec.AnyFlatSpec
import org.scalatest.matchers.should.Matchers

import cats.effect.IO
import cats.effect.unsafe.implicits.global

import config._
import slick.jdbc.SQLiteProfile.api._

import java.util.UUID
import scala.concurrent.ExecutionContext

class MealServiceSpec extends AnyFlatSpec with Matchers {

  // Test database instance (in-memory SQLite)
  val db = DatabaseConfig.getTestingDatabase()
  implicit val ec: ExecutionContext = scala.concurrent.ExecutionContext.global

  // Fresh database setup before each test
  def setupDatabase(): IO[Unit] = {
    DatabaseConfig.createTestingTables()
  }

  var mealId: UUID = _

  "MealService" should "successfully insert a meal and return the inserted meal" in {
    // Ensure fresh database setup before each test
    setupDatabase().unsafeRunSync()

    // Create a meal input
    val mealInput = MealInput("pizza", 24000, List("cheese", "tomato", "sour"))
    val result: IO[Either[String, Option[Meal]]] = MealService.postMeal(mealInput)
    val evaluatedResult = result.unsafeRunSync()

    // Assert the result is a Right containing the inserted meal (id should be non-empty)
    evaluatedResult match {
      case Right(Some(meal)) =>
        meal.name.shouldBe("pizza")
        meal.servings.shouldBe(24000)
        meal.ingredients.shouldBe(List("cheese", "tomato", "sour"))
        mealId = meal.id
      case _ => fail("Meal insertion failed")
    }
  }

  it should "successfully retrieve the inserted meal by its UUID" in {
    // Ensure the mealId is set before making the GET request
    assert(mealId != null, "Meal ID should not be null")

    // Retrieve the meal by its UUID
    val retrievedMeal: IO[Either[String, Option[Meal]]] = MealService.getMeal(mealId)
    val evaluatedRetrievedMeal = retrievedMeal.unsafeRunSync()

    // Assert the meal is found and matches the original inserted meal
    evaluatedRetrievedMeal match {
      case Right(Some(meal)) =>
        meal.id shouldBe mealId // Ensure the correct meal ID
        meal.name shouldBe "pizza"
        meal.servings shouldBe 24000
        meal.ingredients shouldBe List("cheese", "tomato", "sour")
      case _ => fail("Meal retrieval failed")
    }
  }

  it should "successfully update the inserted meal via patch" in {
    // Ensure the mealId is set before making the PATCH request
    assert(mealId != null, "Meal ID should not be null")

    // Create a meal input with updated values
    val updatedMealInput = MealInput("Updated Pizza", 30000, List("cheese", "tomato", "olives"))

    // Perform the patch operation
    val patchResult: IO[Either[String, Option[Meal]]] = MealService.patchMeal(mealId, updatedMealInput)
    val evaluatedPatchResult = patchResult.unsafeRunSync()

    // Assert the patch operation was successful
    evaluatedPatchResult match {
      case Right(Some(meal)) =>
        meal.id shouldBe mealId  // Ensure the correct meal ID is returned
        meal.name shouldBe "Updated Pizza"  // Assert updated name
        meal.servings shouldBe 30000  // Assert updated servings
        meal.ingredients shouldBe List("cheese", "tomato", "olives")  // Assert updated ingredients
      case _ => fail("Meal patching failed")
    }
  }

  it should "successfully delete the inserted meal by its UUID" in {
    // Ensure the mealId is set before making the DELETE request
    assert(mealId != null, "Meal ID should not be null")

    // Perform the delete operation
    val deleteResult: IO[Either[String, Unit]] = MealService.deleteMeal(mealId)
    val evaluatedDeleteResult = deleteResult.unsafeRunSync()

    // Assert the delete operation was successful
    evaluatedDeleteResult match {
      case Right(_) =>
        // After deleting, ensure the meal cannot be found
        val retrievedMealAfterDelete: IO[Either[String, Option[Meal]]] = MealService.getMeal(mealId)
        val evaluatedRetrievedMealAfterDelete = retrievedMealAfterDelete.unsafeRunSync()

        // Assert the meal is no longer found
        evaluatedRetrievedMealAfterDelete match {
          case Right(None) =>
            // Confirm that the meal was deleted (no meal should be found)
          case _ => fail("Meal was not deleted successfully")
        }

      case Left(error) => fail(s"Meal deletion failed: $error")
    }
  }
}
