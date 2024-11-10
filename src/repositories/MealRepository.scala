package repositories

import models._
import bases._

import cats.effect.{ExitCode, IO, IOApp}
import slick.jdbc.SQLiteProfile.api._

object MealRepository {

  // Insert a meal into the database
  def insertMeal(meal: Meal): DBIO[Int] = {
    val query = TableQuery[Meals] // Assuming Meals is the table
    query += meal // This will insert the meal into the database
  }

  // Other database-related methods
  def getAllMeals(): DBIO[Seq[Meal]] = TableQuery[Meals].result
}
