package repositories

import config._
import models._
import bases._

import cats.effect.IO
import slick.jdbc.SQLiteProfile.api._
import io.circe.KeyEncoder.encodeKeyUUID

import java.util.UUID

object MealRepository {

  // Other database-related methods
  def retrieveAllMeals(): DBIO[Option[Seq[Meal]]] = {
    val query = TableQuery[Meals]
    val meals = query.result.map(Option(_))
    meals
  }

  // Insert a meal into the database
  def insertMeal(meal: MealInput): DBIO[Option[Meal]] = {
    val query = TableQuery[Meals]

    // Create the meal object with a randomly generated ID
    val mealWithId = Meal(UUID.randomUUID(), meal.name, meal.servings, meal.ingredients)

    // Insert the meal into the database and return the inserted meal (with generated ID)
    for {
      _ <- query += mealWithId // Insert the meal into the database
      insertedMeal <- retrieveMealById(mealWithId.id) // Fetch the meal by its generated ID
    } yield insertedMeal
  }

  def retrieveMealById(id: UUID): DBIO[Option[Meal]] = {
    val query = TableQuery[Meals]
    val meal = query.filter(_.id === id).result.headOption
    meal
  }

  def removeMealById(id: UUID): DBIO[Option[Meal]] = {
    val query = TableQuery[Meals]
  
    // Step 1: Fetch the meal if it exists
    val mealQuery = query.filter(_.id === id).result.headOption

    // Step 2: If meal exists, delete it and return the meal
    mealQuery.flatMap {
      case Some(meal) =>
        // Meal exists, so delete it
        query.filter(_.id === id).delete.map {
          case 0 => None  // If no rows were deleted
          case _ => Some(meal)  // Return the deleted meal
        }
      case None => DBIO.successful(None)  // Meal not found
    }
  }

  def updateMeal(id: UUID, meal: MealInput): DBIO[Option[Meal]] = {
    val query = TableQuery[Meals]
    
    // Find the existing meal
    val existingMealQuery = query.filter(_.id === id).result.headOption

    // Update the meal if it exists
    existingMealQuery.flatMap {
      case Some(existingMeal) =>
        // Create a new meal with the updated information
        val updatedMeal = existingMeal.copy(
          name = meal.name,
          servings = meal.servings,
          ingredients = meal.ingredients
        )

        // Update the meal in the database and return the updated meal
        query.filter(_.id === id).update(updatedMeal).map(_ => Some(updatedMeal))

      case None => DBIO.successful(None)
    }
  }
}
