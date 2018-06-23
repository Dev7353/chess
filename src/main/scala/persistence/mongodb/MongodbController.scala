package persistence.mongodb

import controller.Controller
import model._
import org.mongodb.scala._
import org.mongodb.scala.model.CreateCollectionOptions
import Helpers._
import org.mongodb.scala.connection.NettyStreamFactoryFactory

case class MongodbController(controller: Controller) {
  System.setProperty("org.mongodb.async.type", "netty")
  val mongoClient: MongoClient = MongoClient()
  val database: MongoDatabase = mongoClient.getDatabase("ChessCluster")
  def load(sessionid: Int): Unit = {
    print("Test")
  }

  def save(): Unit = {
    val collection: MongoCollection[Document] = database.getCollection("test")
    collection.drop().results()
    val doc: Document = Document("_id" -> 0, "name" -> "MongoDB", "type" -> "database",
      "count" -> 1, "info" -> Document("x" -> 203, "y" -> 102))
    collection.insertOne(doc).results()

    // get it (since it's the only one in there since we dropped the rest earlier on)
    collection.find.first().printResults()
  }
}
