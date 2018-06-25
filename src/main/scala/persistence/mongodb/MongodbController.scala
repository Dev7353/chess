package persistence.mongodb

import java.util.concurrent.TimeUnit

import controller.Controller
import org.mongodb.scala._
import Helpers._
import _root_.model._
import com.mongodb.BasicDBObject
import org.mongodb.scala.bson.collection.immutable.Document
import org.mongodb.scala.model.Filters._
import org.mongodb.scala.bson.conversions
import play.api.libs.json.{JsArray, Json}

import scala.collection.mutable.ListBuffer
import scala.concurrent.Await
import scala.concurrent.duration.Duration

case class MongodbController(controller: Controller) {
  System.setProperty("org.mongodb.async.type", "netty")
  //val mongoClient: MongoClient = MongoClient()
  val mongoClient: MongoClient = MongoClient("mongodb+srv://Stef:5RnCuYQEm3deXvx7@chesscluster-6z5vv.mongodb.net/test?retryWrites=true")

  val database: MongoDatabase = mongoClient.getDatabase("ChessCluster")
  val collection: MongoCollection[Document] = database.getCollection("sessions")

  def load(name: String): Unit = {

    val session = collection.find(equal("name", name)).subscribe(new Observer[Document] {
      override def onNext(result: Document): Unit = {
        val session = Json.parse(result.toJson())
        controller.round = (session \ "session" \ "round").get.toString().toInt
        var listA = new ListBuffer[Figure]()
        var listB = new ListBuffer[Figure]()
        var gf = new GameField
        controller.setPlayerA(new Player((session \ "p1").get.toString().replaceAll("\"", "")))
        controller.setPlayerB(new Player((session \ "p2").get.toString().replaceAll("\"", "")))
        val gamefield = (session \ "gamefield").get.as[JsArray]


        for (e <- gamefield.value) {
          val player = (e \ "Player").get.toString().replaceAll("\"", "") //name
          val piece = (e \ "designator").get.toString().replaceAll("\"", "") //designator
          val x = (e \ "Position" \ "x").get.toString().toInt
          val y = (e \ "Position" \ "y").get.toString().toInt
          val pos = (x, y) //position


          if (player == controller.playerA.toString) {

            val f: Figure = getFigure(piece, pos, "UP")
            gf.set(pos, f)
            listA += f
          } else {
            val f: Figure = getFigure(piece, pos, "DOWN")
            gf.set(pos, f)
            listB += f
          }
        }

        for {
          i <- 0 to 7
          j <- 0 to 7
        } yield {
          if (gf.get((i, j)) == null) {
            gf.set((i, j), Figure())
          }
        }

        controller.playerA.figures = listA
        controller.playerB.figures = listB
        controller.gamefield = gf


        for {
          i <- 0 to 7
          j <- 0 to 7
        } yield {
          if (gf.get((i, j)) == null) {
            gf.set((i, j), Figure())
          }
        }
        controller.playerA.figures = listA
        controller.playerB.figures = listB
        controller.gamefield = gf
      }

      override def onError(e: Throwable): Unit = println("ERROR! " + e)

      override def onComplete(): Unit = println("Completed!")
    })
  }

  def save(name: String): Unit = {
    val p1 = controller.playerA.toString
    val p2 = controller.playerB.toString
    val session = Document("id" -> 0, "name" -> name, "round" -> controller.round, "PlayerA" -> p1, "PlayerB" -> p2)
    var field = new ListBuffer[Document]

    for {
      i <- 0 to 7
      j <- 0 to 7
    } yield {
      val fig = controller.gamefield.get(i, j)
      if (controller.playerA.hasFigure(fig)) {
        field += Document("id" -> 0, "designator" -> fig.toString(), "Position" -> Document("x" -> i, "y" -> j), "Player" -> p1)
      }

      if (controller.playerB.hasFigure(fig)) {
        field += Document("id" -> 0, "designator" -> fig.toString(), "Position" -> Document("x" -> i, "y" -> j), "Player" -> p2)
      }
    }

    collection.drop().results()
    val doc: Document = Document("sessionid" -> 0, "p1" -> p1, "p2" -> p2, "session" -> session,
      "gamefield" -> field)
    collection.insertOne(doc).results()

    // get it (since it's the only one in there since we dropped the rest earlier on)
    collection.find.first().printResults()
  }

  def getFigure(piece: String, pos: Tuple2[Int, Int], direction: String): Figure = {
    piece match {
      case "B" => direction match {
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
}
