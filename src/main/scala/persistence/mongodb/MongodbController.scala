package persistence.mongodb

import controller.Controller
import org.mongodb.scala._
import Helpers._
import org.mongodb.scala.bson.collection.immutable.Document
import org.mongodb.scala.model.Filters._
import scala.collection.mutable.ListBuffer

case class MongodbController(controller: Controller) {
  System.setProperty("org.mongodb.async.type", "netty")
  //val mongoClient: MongoClient = MongoClient()

  val database: MongoDatabase = mongoClient.getDatabase("ChessCluster")
  val collection: MongoCollection[Document] = database.getCollection("sessions")
  def load(sessionid: Int): Unit = {
    collection.find(equal("sessionid", 0)).printHeadResult()
  }

  def save(): Unit = {
    val p1 = controller.playerA.toString
    val p2 = controller.playerB.toString
    val session = Document("id" -> 0, "round" -> controller.round, "PlayerA" -> p1, "PlayerB" -> p2)
    var field = new ListBuffer[Document]

    for{
      i <- 0 to 7
      j <- 0 to 7
    }yield {
      val fig = controller.gamefield.get(i, j)
      if(controller.playerA.hasFigure(fig)){
        field += Document("id" -> 0, "designator" -> fig.toString(), "Position" -> Document("x"-> i, "y" -> j), "Player" -> p1)
      }

      if(controller.playerB.hasFigure(fig)){
        field += Document("id" -> 0, "designator" -> fig.toString(), "Position" -> Document("x"-> i, "y" -> j), "Player" -> p2)
      }
    }

    collection.drop().results()
    val doc: Document = Document("sessionid" -> 0, "p1" -> p1, "p2" -> p2,
      "gamefield" -> field)
    collection.insertOne(doc).results()

    // get it (since it's the only one in there since we dropped the rest earlier on)
    collection.find.first().printResults()
  }
}
