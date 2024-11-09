package services

import bases._
import models._
import config._

import cats.effect.{ExitCode, IO, IOApp}
import slick.jdbc.SQLiteProfile.api._

import scala.concurrent.ExecutionContext

// Define an implicit ExecutionContext for async operations
implicit val ec: ExecutionContext = scala.concurrent.ExecutionContext.global

object MealService {

  // Database instance
  val db = DatabaseConfig.getDatabase()

  // Insert meal DBIO action
  def insertMeal(meal: Meal): DBIO[Int] = {
    val query = TableQuery[Meals]  // Assuming Meals is the table
    query += meal  // This will insert the meal into the database
  }

  // Service method to insert a meal and return an IO
  def postMeal(meal: Meal): IO[Either[String, Meal]] = {
    val insertMealAction = insertMeal(meal)

    IO.fromFuture(IO {
      db.run(insertMealAction).transform {
        case scala.util.Success(_) => scala.util.Success(Right(meal))  // Successfully inserted
        case scala.util.Failure(ex) => scala.util.Success(Left(s"Error inserting meal: ${ex.getMessage}"))  // Handle error
      }
    })
  }
}
