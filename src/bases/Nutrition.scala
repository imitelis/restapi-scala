package bases

import com.github.plokhotnyuk.jsoniter_scala.macros.* // needed for derives
import sttp.tapir.Schema

case class Nutrition(name: String, healthy: Boolean, calories: Int)
  derives ConfiguredJsonValueCodec, Schema