package view

import controller.Controller
import model.GameField
import observer.Observer

class TextUi(field: GameField, controller: Controller) extends Observer{

  controller.add(this)
  def update = draw


  def toHtml(): String={
    val sb = new StringBuilder()

    sb.++=("Player: " + controller.currentPlayer + "<br>")
    sb.++=(field.toString + "<br>")
    sb.++=("0 &nbsp; 1 &nbsp; 2  &nbsp;3 &nbsp; 4 &nbsp; 5 &nbsp; 6 &nbsp;7<br>")
    sb.++=("Player: " + controller.enemyPlayer + "<br>")

    sb.toString()

  }

  def draw(): Unit ={

    print("Player: " + controller.currentPlayer + "\n")
    print(field.toString + "\n")
    print("0\t1\t2\t3\t4\t5\t6\t7\n")
    print("Player: " + controller.enemyPlayer + "\n")

  }
}
