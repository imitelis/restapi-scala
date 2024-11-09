// src/config/Database.scala
package config

import java.sql.{Connection, DriverManager}

import slick.jdbc.SQLiteProfile.api._
import scala.concurrent.Await
import scala.concurrent.duration._

import models._

import slick.jdbc.SQLiteProfile.api._
import slick.dbio.DBIO
import scala.concurrent.{Await, Future}
import java.sql.Connection
import java.sql.DriverManager

object DatabaseConfig {
  // Database URL for SQLite
  val dbUrl = "jdbc:sqlite:meals.db"

  // Load SQLite JDBC driver
  Class.forName("org.sqlite.JDBC")

  // Create a database connection
  def createConnection(): Connection = {
    DriverManager.getConnection(dbUrl)
  }

  // Create and return a Slick Database instance
  def getDatabase(): Database = {
    Database.forURL(dbUrl, driver = "org.sqlite.JDBC")
  }

  // Create the meals table if it doesn't already exist
  def createTables(): Unit = {
    val db = getDatabase()
    
    // Run the migration to create the table
    val setup = DBIO.seq(
      Meals.query.schema.createIfNotExists // Create table if it doesn't exist
    )

    // Execute the setup action
    val setupFuture = db.run(setup)
    Await.result(setupFuture, scala.concurrent.duration.Duration(2, "seconds")) // Wait for the setup to complete

    db.close() // Close the database connection
  }
}