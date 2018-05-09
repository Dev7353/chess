package controller

import akka.actor.ActorSystem
import akka.http.scaladsl.Http
import akka.http.scaladsl.model._
import akka.http.scaladsl.server.Directives._
import akka.stream.ActorMaterializer
import com.sun.deploy.net.HttpResponse

import scala.io.StdIn

class ControllerWebService(c: Controller) {

  implicit val system = ActorSystem("my-system")
  implicit val materializer = ActorMaterializer()
  // needed for the future flatMap/onComplete in the end
  implicit val executionContext = system.dispatcher

  val route = {
    pathPrefix("controller") {
      path("round") {
        complete(200 -> c.round.toString())
      } ~
      path("currentPlayer") {
        complete(200 -> c.currentPlayer.toString)
      } ~
        path("enemyPlayer") {
          complete(200 -> c.enemyPlayer.toString)
        } ~
        path("source") {
          complete(200 -> c.source.toString)
        } ~
        path("target") {
          complete(200 -> c.target.toString)
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
