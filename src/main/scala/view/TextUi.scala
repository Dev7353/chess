package view

import controller.Controller
import model.GameField
import observer.Observer

class TextUi(playerA: String, playerB: String, field: GameField, controller: Controller) extends Observer{

  controller.add(this)
  def update = draw

  def draw(): Unit ={

    print("Player: " + playerA + "\n")
    print(field.toString + "\n")
    print("0\t1\t2\t3\t4\t5\t6\t7\n")
    print("Player: " + playerB + "\n")

  }
}
