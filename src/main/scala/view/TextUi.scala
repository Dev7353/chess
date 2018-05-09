package view

import controller.Controller
import model.GameField
import observer.Observer
import akka.actor.ActorSystem
import akka.http.scaladsl.Http
import akka.http.scaladsl.model._
import akka.http.scaladsl.unmarshalling.Unmarshal
import akka.stream.ActorMaterializer


import scala.concurrent.duration.Duration
import scala.concurrent.{Await, Future}
import scala.util.{Failure, Success}

class TextUi() extends Observer{

  val TAB: String = "\t"
  val NEWLINE: String = "\n"

  //controller.add(this)
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

    val getRound: Future[HttpResponse] = Http().singleRequest(HttpRequest(uri = "http://localhost:8080/controller/round"))
    val getCurrentPlayer: Future[HttpResponse] = Http().singleRequest(HttpRequest(uri = "http://localhost:8080/controller/currentPlayer"))
    val getEnemyPlayer: Future[HttpResponse] = Http().singleRequest(HttpRequest(uri = "http://localhost:8080/controller/enemyPlayer"))
    val getGamefield: Future[HttpResponse] = Http().singleRequest(HttpRequest(uri = "http://localhost:8080/controller/gamefield"))

    Await.result(getRound, Duration.Inf)
    Await.result(getCurrentPlayer, Duration.Inf)
    Await.result(getEnemyPlayer, Duration.Inf)
    Await.result(getGamefield, Duration.Inf)

    val round = Unmarshal(getRound.value.get.get.entity).to[String].value.get.get.toInt
    val currentPlayer = Unmarshal(getCurrentPlayer.value.get.get.entity).to[String].value.get.get.toString
    val enemyPlayer = Unmarshal(getEnemyPlayer.value.get.get.entity).to[String].value.get.get.toString
    val gamefield = Unmarshal(getGamefield.value.get.get.entity).to[String].value.get.get.toString

    sb.++=("Round: " + round + NEWLINE)
    sb.++=("Player: " + currentPlayer + NEWLINE)
    sb.++=(gamefield.toString + NEWLINE)
    sb.++=("0"+TAB+"1"+TAB+"2"+TAB+"3"+TAB+"4"+TAB+"5"+TAB+"6"+TAB+"7" + NEWLINE)
    sb.++=("Player: " + enemyPlayer + NEWLINE)

    sb.toString()
  }
}
