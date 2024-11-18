package services

import bases._
import cats.effect.{ExitCode, IO, IOApp}

object GreetService {
  def getWelcome(): IO[Either[String, Greeting]] = {
    val greetJson = Greeting("Welcome from Scala server!")

    IO.pure(Right(greetJson))
  }
}