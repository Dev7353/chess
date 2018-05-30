package persistence.slick.schema
import slick.jdbc.MySQLProfile.api._
 case class Player (PlayerID: Int, Name: String)

 class PlayerTable(tag: Tag) extends Table[Player](tag, "Player"){

  def PlayerID = column[Int]("PlayerID", O.PrimaryKey, O.AutoInc)
  def Name    = column[String]("Name")

  def *       = (PlayerID, Name).mapTo[Player]
}