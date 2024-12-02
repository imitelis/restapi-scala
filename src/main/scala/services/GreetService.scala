package services

import bases._
import cats.effect.IO

object GreetService {
  def getWelcome(): IO[Either[String, Greeting]] = {
    val greetJson = Greeting("Welcome from Scala server!")

    IO.pure(Right(greetJson))
  }
}