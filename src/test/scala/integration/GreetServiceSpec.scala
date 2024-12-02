import bases._
import services._

import io.circe.generic.auto._
import io.circe.parser.decode
import io.circe.syntax._

import org.scalatest.flatspec.AnyFlatSpec
import org.scalatest.matchers.should.Matchers

import cats.effect.{IO}
import cats.effect.unsafe.implicits.global

import java.util.UUID

class GreetServiceSpec extends AnyFlatSpec with Matchers {

  "GreetService" should "return a Greeting with the correct message" in {
    val result: IO[Either[String, Greeting]] = GreetService.getWelcome()
    val evaluatedResult = result.unsafeRunSync() // blocking, to evaluate IO

    evaluatedResult should be(Right(Greeting("Welcome from Scala server!")))
  }
}