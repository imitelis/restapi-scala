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
    val postAction = MealRepository.insertMeal(meal)

    IO.fromFuture(IO {
      db.run(postAction).map {
        case Some(meal) => Right(Some(meal))  // Meal found
        case None       => Right(None)
      }.recover {
        case ex: Exception => Left(s"Error getting meal: ${ex.getMessage}") // Handle error
      }
    })
  }

  def getMeals(): IO[Either[String, Option[Seq[Meal]]]] = {
    val getAction = MealRepository.retrieveAllMeals()

    IO.fromFuture(IO {
      db.run(getAction).map {
        case Some(meals) => Right(Some(meals))  // Meal found
        case None        => Right(None)
      }.recover {
        case ex: Exception => Left(s"Error getting meal: ${ex.getMessage}") // Handle error
      }
    })
  }

  def getMeal(mealUuid: UUID): IO[Either[String, Option[Meal]]] = {
    val getAction = MealRepository.retrieveMealById(mealUuid)

    IO.fromFuture(IO {
      db.run(getAction).map {
        case Some(meal) => Right(Some(meal))
        case None       => Right(None)
      }.recover {
        case ex: Exception => Left(s"Error getting meal: ${ex.getMessage}") // Handle error
      }
    })
  }

  def patchMeal(mealUuid: UUID, meal: MealInput): IO[Either[String, Option[Meal]]] = {
  val patchAction = MealRepository.updateMeal(mealUuid, meal)

  IO.fromFuture(IO {
    db.run(patchAction).map {
      case Some(meal) => Right(Some(meal))
      case None       => Right(None)
    }.recover {
      case ex: Exception => Left(s"Error updating meal: ${ex.getMessage}") // Handle error
    }
  })
}

  def deleteMeal(mealUuid: UUID): IO[Either[String, Unit]] = {
    val deleteAction = MealRepository.removeMealById(mealUuid)

    IO.fromFuture(IO {
      db.run(deleteAction).map {
        case Some(_) => Right(())  // Meal deleted successfully
        case None    => Left("Meal not found")  // No meal found (not deleted)
      }.recover {
        case ex: Exception => Left(s"Error deleting meal: ${ex.getMessage}") // Handle error
      }
    })
  }
}