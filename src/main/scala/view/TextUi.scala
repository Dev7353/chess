package view

import   model.GameField

class TextUi(playerA: String, playerB: String, field: GameField) {

  def draw(): Unit ={

    print(playerA)
    print("___________________________________________________________\n")
    print(field.toString)
    print("___________________________________________________________\n")
    print(playerB)

  }
}
