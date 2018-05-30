package persistence.slick
import akka.actor.ActorSystem
import akka.http.scaladsl.Http
import akka.http.scaladsl.marshallers.sprayjson.SprayJsonSupport
import akka.http.scaladsl.server.Directives._
import akka.stream.ActorMaterializer
import model.Player
import spray.json._

import scala.io.StdIn

object slickMicroService {
  implicit val system = ActorSystem("my-system")
  implicit val materializer = ActorMaterializer()
  // needed for the future flatMap/onComplete in the end
  implicit val executionContext = system.dispatcher
  def main(args: Array[String]): Unit = {
    val route = {
      pathPrefix("slick") {
          path("load") {
            complete(200 -> "lel")
          }~
          path("save") {
            parameters('playerOne, 'chessPieceMapOne, 'playerTwo, 'chessPieceMapTwo){ (playerOne, chessPieceMapOne, playerTwo, chessPieceMapTwo) =>
              complete(200 -> "lel")
            }
          }~
          path("exit") {
            complete(200 -> "lel")
          }
      }
    }

    val bindingFuture = Http().bindAndHandle(route, "localhost", 8081)

    println(s"Server online at http://localhost:8081/\nPress RETURN to stop...")
    StdIn.readLine() // let it run until user presses return
    bindingFuture
      .flatMap(_.unbind()) // trigger unbinding from the port
      .onComplete(_ => system.terminate()) // and shutdown when done

  }
}
