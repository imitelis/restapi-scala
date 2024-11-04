
// main.scala

import cats.effect.{ExitCode, IO, IOApp}
import cats.syntax.all.*
import org.http4s.HttpRoutes
import org.http4s.blaze.server.BlazeServerBuilder
import org.http4s.server.Router
import sttp.tapir.*
import sttp.shared.Identity
import sttp.tapir.server.http4s.Http4sServerInterpreter
import sttp.tapir.swagger.bundle.SwaggerInterpreter

import config._
import bases._
import routers._

object main extends IOApp:
  val serverEndpoints = List(MealRouter.mealEndpoint)

  val serverRoutes: HttpRoutes[IO] = Http4sServerInterpreter[IO]()
    .toRoutes(serverEndpoints)

  val swaggerEndpoints = SwaggerInterpreter()
    .fromServerEndpoints[IO](serverEndpoints, "Meals API", "1.0")

  val swaggerRoutes: HttpRoutes[IO] = Http4sServerInterpreter[IO]().toRoutes(swaggerEndpoints)

  val allRoutes: HttpRoutes[IO] = serverRoutes <+> swaggerRoutes

  // Create a connection to the database
  val connection = DatabaseConfig.createTables()

  println("Database created at: meals.db")

  override def run(args: List[String]): IO[ExitCode] =
    BlazeServerBuilder[IO]
      .bindHttp(8080, "0.0.0.0")
      .withHttpApp(Router("/" -> allRoutes).orNotFound)
      .resource
      .use(_ => IO.never)
      .as(ExitCode.Success)