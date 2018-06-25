package persistence.mongodb

import java.util.concurrent.TimeUnit

import play.api.libs.json
import controller.Controller
import org.mongodb.scala._
import Helpers._
import _root_.model._
import com.mongodb.BasicDBObject
import org.mongodb.scala.bson.collection.immutable.Document
import org.mongodb.scala.model.Filters._
import org.mongodb.scala.bson.conversions
import play.api.libs.json.Json

import scala.collection.mutable.ListBuffer
import scala.concurrent.Await
import scala.concurrent.duration.Duration

case class MongodbController(controller: Controller) {
  System.setProperty("org.mongodb.async.type", "netty")
  //val mongoClient: MongoClient = MongoClient()

  val database: MongoDatabase = mongoClient.getDatabase("ChessCluster")
  val collection: MongoCollection[Document] = database.getCollection("sessions")

  def load(sessionid: Int): Unit = {

    val session = collection.find(equal("sessionid", 0)).subscribe(new Observer[Document] {
      override def onNext(result: Document): Unit = {
        val session = Json.parse(result.toJson())
        controller.round = (session \ "session" \ "round").get.toString().toInt
        var listA = new ListBuffer[Figure]()
        var listB = new ListBuffer[Figure]()
        var gf = new GameField
        controller.setPlayerA(new Player((session \ "p1").get.toString().replaceAll("\"", "")))
        controller.setPlayerB(new Player((session \ "p2").get.toString().replaceAll("\"", "")))

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
      }

      override def onError(e: Throwable): Unit = println("ERROR! " + e)

      override def onComplete(): Unit = println("Completed!")
    })
  }

  def save(): Unit = {
    val p1 = controller.playerA.toString
    val p2 = controller.playerB.toString
    val session = Document("id" -> 0, "round" -> controller.round, "PlayerA" -> p1, "PlayerB" -> p2)
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
