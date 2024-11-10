package services

import bases._
import models._
import config._
import repositories._

import cats.effect.{ExitCode, IO, IOApp}
import slick.jdbc.SQLiteProfile.api._

import scala.concurrent.ExecutionContext
import scala.util.{Success, Failure}

// Define an implicit ExecutionContext for async operations
implicit val ec: ExecutionContext = scala.concurrent.ExecutionContext.global

object MealService {

  // Database instance
  val db = DatabaseConfig.getDatabase()

  // Service method to insert a meal and return an IO
  def postMeal(meal: Meal): IO[Either[String, Meal]] = {
    val insertMealAction = MealRepository.insertMeal(meal)

    IO.fromFuture(IO {
      db.run(insertMealAction).transform {
        case Success(_) => Success(Right(meal))  // Successfully inserted
        case Failure(ex) => Success(Left(s"Error inserting meal: ${ex.getMessage}"))  // Handle error
      }
    })
  }

  def getMeals(): IO[Either[String, Seq[Meal]]] = {
    val getAllMealsAction = MealRepository.getAllMeals()

    IO.fromFuture(IO {
      db.run(getAllMealsAction).transform {
        case Success(meals) => Success(Right(meals))  // Successfully fetched the meals
        case Failure(ex)   => Success(Left(s"Error fetching meals: ${ex.getMessage}"))  // Handle error
      }
    })
  }
}
