package models

import slick.jdbc.{JdbcType, SQLiteProfile}
import slick.jdbc.SQLiteProfile.api._

import scala.util.Try

import bases._

// Custom JdbcType for List[String]
implicit val listStringType: JdbcType[List[String]] =
  MappedColumnType.base[List[String], String](
    list => list.mkString(","),  // Convert List to String
    str => str.split(",").toList // Convert String back to List
  )

// Define the Slick table for Meal
class Meals(tag: Tag) extends Table[Meal](tag, "meals") {
  def name        = column[String]("name")
  def servings    = column[Int]("servings")
  def ingredients = column[List[String]]("ingredients") // Using List as JSON

  // Use a custom tuple conversion
  def * = (name, servings, ingredients) <> (fromRow, toRow)

  // Custom methods to convert to/from tuple
  private def fromRow: ((String, Int, List[String])) => Meal = {
    case (name, servings, ingredients) => Meal(name, servings, ingredients)
  }

  private def toRow: Meal => (String, Int, List[String]) = { meal =>
    (meal.name, meal.servings, meal.ingredients)
  }
}

// Companion object to access the TableQuery
object Meals {
  val query = TableQuery[Meals]
}
