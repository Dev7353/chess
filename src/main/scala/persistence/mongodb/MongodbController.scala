package persistence.mongodb

import controller.Controller
import model._
import org.mongodb.scala._

case class MongodbController(controller: Controller) {
  def load(sessionid: Int): Unit = {
    val mongoClient: MongoClient = MongoClient()
  }

  def save(): Unit {

  }
}
