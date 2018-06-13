package persistence.slick.schema
import slick.jdbc.MySQLProfile.api._
case class Session (PlayerID: Int, SessionName: String, Round: Int, PlayerAID: Int, PlayerBID: Int)

class SessionTable(tag: Tag) extends Table[Session](tag, "Session"){

  def SessionID    = column[Int]("SessionID", O.PrimaryKey, O.AutoInc)
  def SessionName  = column[String]("Name")
  def Round        = column[Int]("Round")
  def PlayerAID    = column[Int]("PlayerAID")
  def PlayerBID    = column[Int]("PlayerBID")

  def *       = (SessionID, SessionName, Round, PlayerAID, PlayerBID).mapTo[Session]
}