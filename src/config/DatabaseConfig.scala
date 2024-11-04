// src/config/Database.scala
package config

import java.sql.{Connection, DriverManager}

import slick.jdbc.SQLiteProfile.api._
import scala.concurrent.Await
import scala.concurrent.duration._

import models._

object DatabaseConfig {
  // Database URL
  val dbUrl = "jdbc:sqlite:meals.db"

  // Load SQLite JDBC driver
  Class.forName("org.sqlite.JDBC")

  // Create a connection to the database (this will create the file if it doesn't exist)
  def createConnection(): Connection = {
    DriverManager.getConnection(dbUrl)
  }

  // Create the meals table
  def createTables(): Unit = {
    val db = Database.forURL(dbUrl, driver = "org.sqlite.JDBC")
    
    // Run the migration to create the table
    val setup = DBIO.seq(
      Meals.query.schema.createIfNotExists // Create table if it doesn't exist
    )

    // Execute the setup action
    val setupFuture = db.run(setup)
    Await.result(setupFuture, 2.seconds) // Wait for the setup to complete

    db.close() // Close the database connection
  }
}
