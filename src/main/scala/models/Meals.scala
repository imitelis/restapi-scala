package models

import slick.jdbc.{JdbcType, SQLiteProfile}
import slick.jdbc.SQLiteProfile.api._

import java.util.UUID
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

  def id = column[UUID]("id", O.PrimaryKey)
  def name = column[String]("name")
  def servings = column[Int]("servings")
  def ingredients = column[List[String]]("ingredients")

  // Mapping the row to Meal case class
  def * = (id, name, servings, ingredients) <> (fromRow, toRow)

  // Convert from Row (handle Option[UUID])
  private def fromRow: ((UUID, String, Int, List[String])) => Meal = {
    case (id, name, servings, ingredients) => Meal(id, name, servings, ingredients)
  }

  // Convert to Row (handle Option[UUID] properly)
  private def toRow: Meal => (UUID, String, Int, List[String]) = { meal =>
    (meal.id, meal.name, meal.servings, meal.ingredients)
  }
}

// Companion object to access the TableQuery
object Meals {
  val query = TableQuery[Meals]
}
