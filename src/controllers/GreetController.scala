package controllers

import sttp.tapir._
import sttp.tapir.json.jsoniter.* // needed for jsonBody
import cats.syntax.all.*
import cats.effect.{ExitCode, IO, IOApp}

import bases._
import services._

object GreetController {
    val helloEndpoint = endpoint.get
    .in("")
    .out(jsonBody[Greeting])
    .serverLogic[IO] { _ =>
        GreetService.getWelcome().map {
            case Right(greetJson) => Right(greetJson)
            case Left(errorMessage)   => Left(())
        }
    }
    .tag("hello")
}