package persistence.slick.schema
import slick.jdbc.MySQLProfile.api._
case class ChessPiece (
                            PieceID: Int,
                            Designator: String,
                            Position: String,
                            PlayerID: Int
                        )

case class ChessPieceTable(tag: Tag) extends Table[ChessPiece](tag, "ChessPiece"){

  def PieceID     = column[Int]("PieceID", O.PrimaryKey, O.AutoInc)
  def Designator  = column[String]("Designator")
  def Position    = column[String]("Position")
  def PlayerID    = column[Int]("PlayerID")

  def *       = (PieceID, Designator, Position, PlayerID).mapTo[ChessPiece]

  def Player = foreignKey("Player_FK", PlayerID, TableQuery[PlayerTable])(_.PlayerID)
}