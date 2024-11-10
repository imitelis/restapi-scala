package controllers

import sttp.tapir._
import cats.syntax.all.*
import cats.effect.{ExitCode, IO, IOApp}

import io.circe.generic.auto._  // For automatic Encoder/Decoder derivation
import sttp.tapir.json.circe._  // For Tapir-Circe integration
import sttp.tapir.generic.auto._  // <-- Add this import for automatic Schema derivation

import scala.util.Random

import config._
import bases._
import services._
import repositories._

object MealController {

  val postEndpoint = endpoint.post
    .in("meals")
    .in(jsonBody[Meal])  // Accept a Meal object in the request body
    .out(jsonBody[Meal])  // Return the created Meal as response
    .serverLogic[IO] { meal =>
      // Call MealService to insert the meal and return the result
      MealService.postMeal(meal).map {
        case Right(createdMeal) => Right(createdMeal)  // Return the created meal
        case Left(errorMessage) => Left(())  // Handle error (you could improve this with more detailed error handling)
      }
    }
    .tag("meals")

    val getEndpoint = endpoint.get
    .in("meals")
    .out(jsonBody[Seq[Meal]])  // Return a list of meals in the response
    .serverLogic[IO] { _ =>
      // Call MealService to get all meals
      MealService.getMeals().map {
        case Right(meals) => Right(meals)  // Return the list of meals
        case Left(errorMessage) => Left(())
      }
    }
    .tag("meals")
}