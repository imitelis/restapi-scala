package bases

import io.circe.generic.auto._
import java.util.UUID

case class Meal(id: UUID, name: String, servings: Int, ingredients: List[String])
