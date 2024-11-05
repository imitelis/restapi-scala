import scala.util.Random

import bases._
import cats.effect.{ExitCode, IO, IOApp}

object MealService {
  def getNutrition(meal: Meal): IO[Either[String, Nutrition]] = {
    val random = new Random
    val nutritionInfo = Nutrition(meal.name, random.nextBoolean(), random.nextInt(1000))
    
    IO.pure(Right(nutritionInfo)) // Simulating a service call that returns nutrition info
  }
}
