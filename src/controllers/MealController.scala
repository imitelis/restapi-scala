package controllers

import sttp.tapir._
import sttp.tapir.json.jsoniter.* // needed for jsonBody
import cats.syntax.all.*
import cats.effect.{ExitCode, IO, IOApp}

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
}