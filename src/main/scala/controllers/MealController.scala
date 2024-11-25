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


import sttp.tapir.server._
import org.http4s.Status

object MealController {

  val postEndpoint = endpoint.post
    .in("meals")
    .in(jsonBody[MealInput])  // Accept a Meal object in the request body
    .out(jsonBody[Option[Meal]]) // Return the created Meal as response
    .serverLogic[IO] { meal =>
      // Call MealService to insert the meal and return the result
      MealService.postMeal(meal).map {
        case Right(newMeal)     => Right(newMeal) // Return the created meal
        case Left(errorMessage) => Left(Status.InternalServerError) // Handle error
      }
    }
    .tag("meals")

  val getsEndpoint = endpoint.get
    .in("meals")
    .out(jsonBody[Option[Seq[Meal]]])
    .serverLogic[IO] { _ =>
      MealService.getMeals().map {
        case Right(meals)       => Right(meals)
        case Right(None)        => Left((StatusCode.NotFound, "Meal not found"))
        case Left(errorMessage) => Left(Status.InternalServerError)
      }
    }
    .tag("meals")

  val getEndpoint = endpoint.get
    .in("meals" / path[UUID]("meal_uuid"))
    .out(jsonBody[Option[Meal]])
    .serverLogic[IO] { mealUuid =>
      MealService.getMeal(mealUuid).map {
        case Right(Some(meal))  => Right(Some(meal))
        case Right(None)        => Left((StatusCode.NotFound, "Meal not found"))
        case Left(err)          => Left((StatusCode.InternalServerError, err))
      }
    }
    .tag("meals")

  val patchEndpoint = endpoint.patch
    .in("meals" / path[UUID]("meal_uuid"))
    .in(jsonBody[MealInput])
    .out(jsonBody[Option[Meal]])
    .serverLogic[IO] { (mealUuid, meal) =>
      MealService.patchMeal(mealUuid, meal).map {
        case Right(newMeal) => Right(newMeal)
        case Right(None)    => Left((StatusCode.NotFound, "Meal not found"))
        case Left(err)      => Left((StatusCode.InternalServerError, err))
      }
    }
    .tag("meals")

  val deleteEndpoint = endpoint.delete
    .in("meals" / path[UUID]("meal_uuid"))
    .serverLogic[IO] { mealUuid =>
      MealService.deleteMeal(mealUuid).map {
        case Right(())      => Right(())
        case Left(err)      => Left((StatusCode.InternalServerError, err))
      }
    }
    .tag("meals")

  val mealEndpoints = List(postEndpoint, getsEndpoint, getEndpoint, patchEndpoint, deleteEndpoint)
}
