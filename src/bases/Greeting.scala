// src/bases/greeting.scala
package bases

import com.github.plokhotnyuk.jsoniter_scala.macros.* // needed for derives
import sttp.tapir.Schema

case class Greeting(message: String)
  derives ConfiguredJsonValueCodec, Schema