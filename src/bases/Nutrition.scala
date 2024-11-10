package bases

import io.circe.generic.auto._

case class Nutrition(name: String, healthy: Boolean, calories: Int)