package controllers

import sttp.tapir._
import cats.syntax.all.*
import cats.effect.IO

import io.circe.generic.auto._   // For automatic Encoder/Decoder derivation
import sttp.tapir.json.circe._   // For Tapir-Circe integration
import sttp.tapir.generic.auto._ // <-- Add this import for automatic Schema derivation

import bases._
import services._

object GreetController {
  val helloEndpoint = endpoint.get
    .in("")
    .out(jsonBody[Greeting])
    .serverLogic[IO] { _ =>
      GreetService.getWelcome().map {
        case Right(greetJson)   => Right(greetJson)
        case Left(errorMessage) => Left(())
      }
    }
    .tag("hello")

  val greetEndpoints = List(helloEndpoint)
}
