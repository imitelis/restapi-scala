package routers

import sttp.tapir._
import sttp.tapir.json.jsoniter.* // needed for jsonBody[T]
import cats.effect.{ExitCode, IO, IOApp}
import cats.syntax.all.*

import scala.util.Random

import bases._

object MealRouter {
  val random = new Random

  val mealEndpoint = endpoint.post
  .in("meals")
  .in(jsonBody[Meal])
  .out(jsonBody[Nutrition])
  .serverLogic[IO] { meal =>
    val random = new Random
    val nutritionInfo = Nutrition(meal.name, random.nextBoolean(), random.nextInt(1000))
    
    IO.pure(Right(nutritionInfo))
  }
  .tag("meals")
}