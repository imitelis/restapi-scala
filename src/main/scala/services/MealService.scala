package services

import bases._
import models._
import config._
import repositories._

import cats.effect.{ExitCode, IO, IOApp}
import slick.jdbc.SQLiteProfile.api._

import java.util.UUID
import scala.util.{Success, Failure}

object MealService {

  // Database instance
  val db = DatabaseConfig.getDatabase()

  // Service method to insert a meal and return an IO
  def postMeal(meal: MealInput): IO[Either[String, Option[Meal]]] = {
    val insertMealAction = MealRepository.insertMeal(meal)

    IO.fromFuture(IO {
      db.run(insertMealAction).transform {
        case Success(newMeal)  => Success(Right(newMeal))                                     // Successfully inserted
        case Failure(ex) => Success(Left(s"Error inserting meal: ${ex.getMessage}")) // Handle error
      }
    })
  }

  def getMeals(): IO[Either[String, Seq[Meal]]] = {
    val getAllMealsAction = MealRepository.retrieveAllMeals()

    IO.fromFuture(IO {
      db.run(getAllMealsAction).transform {
        case Success(meals) => Success(Right(meals))                                    // Successfully fetched the meals
        case Failure(ex)    => Success(Left(s"Error fetching meals: ${ex.getMessage}")) // Handle error
      }
    })
  }

  def getMeal(mealUuid: UUID): IO[Either[String, Meal]] = {
    val mealAction = MealRepository.retrieveMealById(mealUuid)

    // Run the DB action and handle the result
    IO.fromFuture(IO {
      db.run(mealAction).map {
        case Some(meal) => Right(meal)  // Meal found
        case None        => Left("Meal not found") // Meal not found
      }
    })
  }

  def deleteMeal(mealUuid: UUID): IO[Either[String, Unit]] = {
    val deleteAction = MealRepository.removeMealById(mealUuid)

    IO.fromFuture(IO {
      db.run(deleteAction).map {
        case _ => Right(()) // Meal deleted successfully
        case None => Left("Meal not found") // No rows affected (i.e., meal not found)
      }
    })
  }
}