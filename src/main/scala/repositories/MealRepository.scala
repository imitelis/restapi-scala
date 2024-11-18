package repositories

import config._
import models._
import bases._

import cats.effect.{ExitCode, IO, IOApp}
import slick.jdbc.SQLiteProfile.api._
import io.circe.KeyEncoder.encodeKeyUUID

import java.util.UUID

object MealRepository {

  // Other database-related methods
  def retrieveAllMeals(): DBIO[Seq[Meal]] = TableQuery[Meals].result

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
    query.filter(_.id === id).result.headOption
  }

  def removeMealById(id: UUID): DBIO[Int] = {
    val query = TableQuery[Meals]
    query.filter(_.id === id).delete  // Deletes meal where ID matches
  }
}
