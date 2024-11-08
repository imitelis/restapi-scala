package bases

import com.github.plokhotnyuk.jsoniter_scala.macros.*
import sttp.tapir.Schema
// import java.time.LocalDate

case class Meal(name: String, servings: Int, ingredients: List[String]) // , requested_date: LocalDate)
  derives ConfiguredJsonValueCodec, Schema
