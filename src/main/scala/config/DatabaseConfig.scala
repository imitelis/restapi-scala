// src/config/Database.scala
package config

import cats.effect.IO
import slick.dbio.DBIO
import slick.jdbc.SQLiteProfile.api._

import models._

import java.sql.{Connection, DriverManager}
import scala.concurrent.duration._
import scala.concurrent.{Await, Future}
import scala.concurrent.ExecutionContext
import scala.concurrent.ExecutionContext.{global}

// Define an implicit ExecutionContext for async operations
implicit val ec: ExecutionContext = global

object DatabaseConfig {
  // Database URL for SQLite
  val dbUrl = sys.env.getOrElse("DB_URL", "jdbc:sqlite:meals.db")
  val dbTestingUrl = sys.env.getOrElse("DB_TESTING_URL", "jdbc:sqlite::memory:")

  // Load SQLite JDBC driver
  Class.forName("org.sqlite.JDBC")

  // Create and return a Slick Database instance
  def getDatabase(): Database = {
    Database.forURL(dbUrl, driver = "org.sqlite.JDBC")
  }

  // Create and return a Slick Database instance
  def getTestingDatabase(): Database = {
    Database.forURL(dbTestingUrl, driver = "org.sqlite.JDBC")
  }

  // Create the meals table if it doesn't already exist
  def createTables(): IO[Unit] = {
    val db = getDatabase()

    // Run the migration to create the table
    val setup = DBIO.seq(
      Meals.query.schema.createIfNotExists // Create table if it doesn't exist
    )

    // Convert the DBIO action to IO and execute it
    IO.fromFuture(IO(db.run(setup)))
      .flatMap(_ => IO.delay(db.close())) // Close the database connection wrapped in IO
      .void // Return IO[Unit] to match the method's return type
  }

  // Create the test-meals table if it doesn't already exist
  def createTestingTables(): IO[Unit] = {
    val db = getTestingDatabase()

    // Run the migration to create the table
    val setup = DBIO.seq(
      Meals.query.schema.createIfNotExists // Create table if it doesn't exist
    )

    // Convert the DBIO action to IO and execute it
    IO.fromFuture(IO(db.run(setup)))
      .flatMap(_ => IO.delay(db.close())) // Close the database connection wrapped in IO
      .void // Return IO[Unit] to match the method's return type
  }
}
