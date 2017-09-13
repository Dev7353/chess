import view.TextUi
import controller.Controller
import model.GameField

object chess {

  def loop(controller: Controller, textUi: TextUi): Unit ={

    textUi.draw()

  }


  def main(args: Array[String]): Unit ={
    // ui, controller objects
    var gamefield: GameField = new GameField()
    print("Please type Name Player A: ")
    val playerA = scala.io.StdIn.readLine()

    print("Please type Name Player B: ")
    val playerB = scala.io.StdIn.readLine()

    var textUi: TextUi = new TextUi(playerA, playerB, gamefield)
    var controller = new Controller(playerA, playerB, gamefield)

    loop(controller, textUi)
  }
}