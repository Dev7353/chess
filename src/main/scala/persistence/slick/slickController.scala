package persistence.slick

import akka.actor.ActorSystem
import akka.actor.Status.{Failure, Success}
import akka.stream.ActorMaterializer
import persistence.slick.schema.{ChessPiece, ChessPieceTable, Player, PlayerTable}
import slick.jdbc.MySQLProfile.api._
import akka.stream.alpakka.slick.scaladsl._
import com.typesafe.config.ConfigFactory
import controller.Controller
import model.Figure

import scala.collection.mutable.ListBuffer
import scala.concurrent.Await
import scala.concurrent.duration.Duration
import scala.concurrent.ExecutionContext.Implicits.global
import scala.util.Try
case class SlickController(controller: Controller) {

  implicit val system = ActorSystem()
  implicit val mat = ActorMaterializer()
  lazy val chessPiece = TableQuery[ChessPieceTable]
  lazy val player = TableQuery[PlayerTable]
  implicit val session = SlickSession.forConfig("chess")

  def load(): Unit ={
    val erg = Await.ready(session.db.run(chessPiece.result), Duration.Inf).value
    //todo: return json of gamefield
  }

  def save(): Unit ={
    val p1 = insertPlayer(Player(0, controller.playerA.toString))
    val p2 = insertPlayer(Player(0, controller.playerB.toString))

    for (i <- 0 until 8){
      for(j <- 0 until 8){
        val fig = controller.gamefield.get(i, j)
        if(controller.playerA.hasFigure(fig)){
          insertChessPiece(ChessPiece(
            0, fig.toString(), "" + i + "" + j, p1
          ))
        }

        if(controller.playerB.hasFigure(fig)){
          insertChessPiece(ChessPiece(
            0, fig.toString(), "" + i + "" + j, p2
          ))
        }
      }

    }

  def insertChessPiece(p: ChessPiece): Unit ={
    Await.ready(
      session.db.run(chessPiece += p),
      Duration.Inf)
    }
  }
  def insertPlayer(p: Player): Int ={
    Await.ready(
      session.db.run(player returning player.map(_.PlayerID) += p),
      Duration.Inf
    ).value.get.get
  }
}
