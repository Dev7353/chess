package view

import controller.Controller
import model.GameField
import observer.Observer

import akka.actor.ActorSystem
import akka.http.scaladsl.Http
import akka.http.scaladsl.model._
import akka.stream.ActorMaterializer

import scala.concurrent.Future
import scala.util.{ Failure, Success }

class TextUi(field: GameField, controller: Controller) extends Observer{

  val TAB: String = "\t"
  val NEWLINE: String = "\n"

  controller.add(this)
  def update = draw


  def toHtml(): String={


    var html: String = getTuiString()

    html = html.replace(NEWLINE, "<br>")
    html = html.replace(TAB, "&nbsp; ")

    html

  }

  def draw(): Unit ={
    print(getTuiString())
  }
  def getTuiString(): String ={
    val sb = new StringBuilder()
    implicit val system = ActorSystem()
    implicit val materializer = ActorMaterializer()
    // needed for the future flatMap/onComplete in the end
    implicit val executionContext = system.dispatcher
    val round: Future[HttpResponse] = Http().singleRequest(HttpRequest(uri = "http://localhost:8080/controller/round"))

    round
      .onComplete {
        case Success(res) => println(res)
        case Failure(_) => sys.error("something wrong")
      }
    sb.++=("Round: " + round + NEWLINE)
    sb.++=("Player: " + controller.currentPlayer + NEWLINE)
    sb.++=(field.toString + NEWLINE)
    sb.++=("0"+TAB+"1"+TAB+"2"+TAB+"3"+TAB+"4"+TAB+"5"+TAB+"6"+TAB+"7" + NEWLINE)
    sb.++=("Player: " + controller.enemyPlayer + NEWLINE)

    sb.toString()
  }
}
