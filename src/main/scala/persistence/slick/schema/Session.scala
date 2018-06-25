package persistence.slick.schema
import slick.jdbc.MySQLProfile.api._
case class Session (SessionName: String, Round: Int, PlayerAID: Int, PlayerBID: Int)

class SessionTable(tag: Tag) extends Table[Session](tag, "Session"){

  def SessionName  = column[String]("SessionName", O.PrimaryKey)
  def Round        = column[Int]("Round")
  def PlayerAID    = column[Int]("PlayerAID")
  def PlayerBID    = column[Int]("PlayerBID")

  def *       = (SessionName, Round, PlayerAID, PlayerBID).mapTo[Session]
}