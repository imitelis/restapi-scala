package bases

import io.circe.generic.auto._

case class MealInput(name: String, servings: Int, ingredients: List[String])
