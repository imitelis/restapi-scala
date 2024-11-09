package bases

import com.github.plokhotnyuk.jsoniter_scala.macros.*
import sttp.tapir.Schema

case class Meal(name: String, servings: Int, ingredients: List[String])
  derives ConfiguredJsonValueCodec, Schema
