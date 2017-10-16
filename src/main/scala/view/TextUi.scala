package view

import controller.Controller
import model.GameField
import observer.Observer

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
    sb.++=("Player: " + controller.currentPlayer + NEWLINE)
    sb.++=(field.toString + NEWLINE)
    sb.++=("0"+TAB+"1"+TAB+"2"+TAB+"3"+TAB+"4"+TAB+"5"+TAB+"6"+TAB+"7" + NEWLINE)
    sb.++=("Player: " + controller.enemyPlayer + NEWLINE)

    sb.toString()
  }
}
