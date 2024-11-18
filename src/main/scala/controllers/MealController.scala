package controllers

import sttp.tapir._
import sttp.model.{HeaderNames, StatusCode}

import cats.syntax.all.*
import cats.effect.{ExitCode, IO, IOApp}

import io.circe.generic.auto._   // For automatic Encoder/Decoder derivation
import sttp.tapir.json.circe._   // For Tapir-Circe integration
import sttp.tapir.generic.auto._ // <-- Add this import for automatic Schema derivation

import java.util.UUID
import scala.util.Random

import config._
import bases._
import services._
import repositories._

object MealController {

  val postEndpoint = endpoint.post
    .in("meals")
    .in(jsonBody[MealInput])  // Accept a Meal object in the request body
    .out(jsonBody[Option[Meal]]) // Return the created Meal as response
    .serverLogic[IO] { meal =>
      // Call MealService to insert the meal and return the result
      MealService.postMeal(meal).map {
        case Right(newMeal) => Right(newMeal) // Return the created meal
        case Left(errorMessage) =>
          Left(()) // Handle error (you could improve this with more detailed error handling)
      }
    }
    .tag("meals")

  val getsEndpoint = endpoint.get
    .in("meals")
    .out(jsonBody[Seq[Meal]]) // Return a list of meals in the response
    .serverLogic[IO] { _ =>
      // Call MealService to get all meals
      MealService.getMeals().map {
        case Right(meals)       => Right(meals) // Return the list of meals
        case Left(errorMessage) => Left(())
      }
    }
    .tag("meals")

  val getEndpoint = endpoint.get
    .in("meals" / path[UUID]("meal_uuid"))
    .out(jsonBody[Meal])
    .serverLogic[IO] { mealUuid =>
      MealService.getMeal(mealUuid).map {
        case Right(meal)       => Right(meal)
        case Left(errorMessage) =>
        Left((
          StatusCode.NotFound, 
          errorMessage
        ))
      }
    }
    .tag("meals")

  val deleteEndpoint = endpoint.delete
    .in("meals" / path[UUID]("meal_uuid"))
    .serverLogic[IO] { mealUuid =>
      MealService.deleteMeal(mealUuid).map {
        case Right(()) => Right(())  // HTTP 204 on success
        case Left(errorMessage) => Left(()) // HTTP 400 or 404 with error message
      }
    }
    .tag("meals")

  val mealEndpoints = List(postEndpoint, getsEndpoint, getEndpoint, deleteEndpoint)
}
