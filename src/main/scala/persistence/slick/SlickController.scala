package persistence.slick

import akka.actor.ActorSystem
import akka.stream.ActorMaterializer
import slick.jdbc.MySQLProfile.api._
import akka.stream.alpakka.slick.scaladsl._
import controller.Controller
import model._
import persistence.slick.schema._

import scala.collection.mutable.ListBuffer
import scala.concurrent.Await
import scala.concurrent.duration.Duration
import scala.util.{Failure, Success}

case class SlickController(controller: Controller) {

  implicit val system = ActorSystem()
  implicit val mat = ActorMaterializer()
  lazy val chessPiece = TableQuery[ChessPieceTable]
  lazy val player = TableQuery[PlayerTable]
  lazy val session = TableQuery[SessionTable]
  implicit val slicksession = SlickSession.forConfig("chess")

  def load(sessionid: Int): Unit ={
    val query = for {
      p <- player
      c <- chessPiece if p.PlayerID === c.PlayerID
      s <- session if (c.PlayerID === s.PlayerAID || c.PlayerID === s.PlayerBID) && s.SessionID === sessionid

    }yield(p.Name, c.Designator, c.Position, s.Round)


    val erg = Await.ready(slicksession.db.run(
      query.result
    ), Duration.Inf).value

    val result: Seq[(String, String, String, Int)] = erg match {
      case Some(k) => k match {
        case Success(l) =>  l
      }
    }
    controller.round = result.head._4
    var listA = new ListBuffer[Figure]()
    var listB = new ListBuffer[Figure]()
    var gf = new GameField
    val names = getNames(result)
    controller.setPlayerA(new model.Player(names._1))
    controller.setPlayerB(new model.Player(names._2))
    if(result.nonEmpty){
      for(e <- result){
        val player = e._1 //name
        val piece = e._2 //designator
        val pos = (e._3.charAt(0).asDigit, e._3.charAt(1).asDigit) //position
        val round = e._4 //round

        if(player == controller.playerA.toString){
          val f: Figure = getFigure(piece, pos, "UP")
          gf.set(pos, f)
          listA += f
        }else{
          val f: Figure = getFigure(piece, pos, "DOWN")
          gf.set(pos, f)
          listB += f
        }
      }

      for{
        i <- 0 to 7
        j <- 0 to 7
      }yield{
        if(gf.get((i,j)) == null){
          gf.set((i,j), Figure())
        }
      }

      controller.playerA.figures = listA
      controller.playerB.figures = listB
      controller.gamefield = gf
    }
  }

  def getFigure(piece: String, pos:Tuple2[Int, Int], direction: String): Figure ={
    piece match {
      case "B" => direction match{
        case "UP" => new Bauer(pos, "DOWN")
        case "DOWN" => new Bauer(pos, "UP")
      }
      case "K" =>
        new König
      case "D" =>
        new Dame
      case "T" =>
        new Turm
      case "L" =>
        new Läufer
      case "O" =>
        new Offizier
    }
  }

  def getNames(l: Seq[Tuple4[String, String, String, Int]]):Tuple2[String, String]={

    var s: Set[String] = Set()
    for(e <- l){
      s += e._1 //name
    }
    (s.head, s.last)
  }

  def save(): Unit ={
    val p1 = insertPlayer(Player(0, controller.playerA.toString))
    val p2 = insertPlayer(Player(0, controller.playerB.toString))
    val s = insertSession(Session(0, "lel", controller.round, p1, p2))
    for{
      i <- 0 to 7
      j <- 0 to 7
    }yield {
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

  def download():Map[String, List[Tuple3[String, Int, Int]]]={
    var result: Map[String, List[Tuple3[String, Int, Int]]] = Map()
    var piecesA: ListBuffer[Tuple3[String, Int, Int]] = ListBuffer[Tuple3[String, Int, Int]]()
    var piecesB: ListBuffer[Tuple3[String, Int, Int]] = ListBuffer[Tuple3[String, Int, Int]]()
    for{
      i <- 0 to 7
      j <- 0 to 7
    }yield{
      val n = controller.getFigure((i,j))
      if(controller.playerA.hasFigure(n)){
        piecesA.+=((n.toString(), i, j))
      } else if(controller.playerB.hasFigure(n)){
        piecesB.+=((n.toString(), i, j))
      }
    }
    result += controller.playerA.toString -> piecesA.toList
    result += controller.playerB.toString -> piecesB.toList
    result
  }

  def insertChessPiece(p: ChessPiece): Unit ={
    Await.ready(
      slicksession.db.run(chessPiece += p),
      Duration.Inf)
    }


  import schema._
  def insertPlayer(p: Player): Int ={
    Await.ready(
      slicksession.db.run(player returning player.map(_.PlayerID) += p),
      Duration.Inf
    ).value.get.get
  }

  def insertSession(s: Session): Int ={
    Await.ready(
      slicksession.db.run(session returning session.map(_.SessionID) += s),
      Duration.Inf
    ).value.get.get
  }
}
