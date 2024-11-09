package controllers

import sttp.tapir._
import sttp.tapir.json.jsoniter.* // needed for jsonBody
import cats.syntax.all.*
import cats.effect.{ExitCode, IO, IOApp}

import scala.util.Random

import bases._
import services._

object MealController {
  val random = new Random

  val mealEndpoint = endpoint.post
  .in("meals")
  .in(jsonBody[Meal])
  .out(jsonBody[Nutrition])
  .serverLogic[IO] { meal =>
    MealService.getNutrition(meal).map {
        case Right(nutritionInfo) => Right(nutritionInfo)
        case Left(errorMessage)   => Left(())
      }
  }
  .tag("meals")
}