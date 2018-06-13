package controller

import akka.actor.ActorSystem
import akka.http.scaladsl.Http
import akka.http.scaladsl.marshallers.sprayjson.SprayJsonSupport
import akka.http.scaladsl.server.Directives._
import akka.stream.ActorMaterializer
import model.Player
import persistence.slick.SlickController
import spray.json._

import scala.io.StdIn

// domain model
final case class JsonPossibleMoves(moves: List[Tuple2[Int, Int]], hasFigure: Boolean)

// collect your json format instances into a support trait:
trait JsonSupport extends SprayJsonSupport with DefaultJsonProtocol {
  implicit val jsonPMFormat = jsonFormat2(JsonPossibleMoves)
}

object ControllerMicroService extends SprayJsonSupport with JsonSupport {

  implicit val system = ActorSystem("my-system")
  implicit val materializer = ActorMaterializer()
  // needed for the future flatMap/onComplete in the end
  implicit val executionContext = system.dispatcher
  val c = new Controller()
  c.setPlayerA(new Player("Rofl"))
  c.setPlayerB(new Player("Kopter"))
  val s = SlickController(c)

  def main(args: Array[String]): Unit = {
    val route = {
      pathPrefix("controller") {
        path("round") {
          complete(c.round.toJson)
        } ~
          path("playerA") {
            complete(200 -> c.playerA.toString)
          }~
          path("playerB") {
            complete(200 -> c.playerB.toString)
          }~
          path("currentPlayer") {
            complete(200 -> c.currentPlayer.toString)
          } ~
          pathPrefix("currentPlayer") {
            path("hasFigure"){
              parameters('x, 'y){ (x, y) =>
                complete(c.currentPlayer.hasFigure(c.gamefield.get((x.toInt,y.toInt))).toJson)
              }
            }
          } ~
          path("enemyPlayer") {
            complete(200 -> c.enemyPlayer.toString)
          } ~
          pathPrefix("enemyPlayer") {
            path("hasFigure"){
              parameters('x, 'y){ (x, y) =>
                complete(c.enemyPlayer.hasFigure(c.gamefield.get((x.toInt,y.toInt))).toJson)
              }
            }
          }~
          path("source") {
            complete(200 -> c.source.toString)
          } ~
          path("target") {
            complete(200 -> c.target.toString)
          }~
          path("gamefield") {
            complete(200 -> c.gamefield.toString)
          }~
          path("possibleMoves") {
            parameters('x, 'y){ (x, y) =>
              complete(c.getPossibleMoves((x.toInt,y.toInt)).toList)
            }
          }~
          path("start") {
            parameters('playerOne, 'playerTwo){ (playerOne, playerTwo) =>
              complete(200-> c.setPlayerA(new Player(playerOne)), c.setPlayerB(new Player(playerTwo)))
            }
          }~
          path("move") {
            parameters('v, 'w, 'x, 'y){ (v, w, x, y) =>
              complete(200 -> c.putFigureTo((v.toInt,w.toInt), (x.toInt,y.toInt)).toString)
            }
          }
      }~
        pathPrefix("slick") {
          path("load") {
            s.load()
            complete(200 -> "load successfully")
          }~
            path("save") {
               s.save()
              complete(200 -> "saved successfully")
            }~
            path("exit") {
              System.exit(0)
              complete(200 -> "EXIT")
            }
        }
    }

    val bindingFuture = Http().bindAndHandle(route, "localhost", 8080)

    println(s"Server online at http://localhost:8080/\nPress RETURN to stop...")
    StdIn.readLine() // let it run until user presses return
    bindingFuture
      .flatMap(_.unbind()) // trigger unbinding from the port
      .onComplete(_ => system.terminate()) // and shutdown when done
  }


}
