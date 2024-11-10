package bases

import io.circe.generic.auto._

case class Meal(name: String, servings: Int, ingredients: List[String])
