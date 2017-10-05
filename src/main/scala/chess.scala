import java.util.Scanner

import view.{GraphicUi, TextUi}
import controller.Controller
import model.{GameField, Player}
import state.{NoAllowedMoveException, NoFigureException, OwnTargetException}

import scala.io.StdIn._
import util.control.Breaks._
import scala.io.StdIn.{readInt, readLine}
object chess {
  def loop(controller: Controller, textUi: TextUi): Unit ={

    val playerA = readLine("Name for Player A: ")
    val playerB = readLine("Name for Player B: ")
    if(controller.playerA == null && controller.playerB == null) {
      controller.setPlayerA(new Player(playerA))
      controller.setPlayerB(new Player(playerB))
      controller.performClick()
    }

    textUi.draw()

    while (true){
      println("----- ROUND "+controller.round+" -----")
      println("\n Select Figure\n")
      var x, y = scala.io.StdIn.readLine()
      println("\n Select Target position\n")
      var t_x, t_y = scala.io.StdIn.readLine()


      var ret = controller.putFigureTo((x.toInt,y.toInt), (t_x.toInt, t_y.toInt))
      if(ret.isInstanceOf[NoFigureException]){
        print("You don't have such a figure!\n")
      } else if(ret.isInstanceOf[NoAllowedMoveException])
        print("You're not supposed to make this move!\n")
      else if(ret.isInstanceOf[OwnTargetException])
        print("You cannot kill your own figures!\n")
      else{

        print("put " + (x,y).toString() + " to " + (t_x, t_y).toString())
        //controller.setNextPlayer()
        print("Next Round. Player ist " + controller.currentPlayer)
      }
    }

  }


  def main(args: Array[String]): Unit ={
    // ui, controller objects
    var gamefield: GameField = new GameField()
    var controller = new Controller(gamefield)
    var textUi: TextUi = new TextUi(gamefield, controller)
    val graphicUi = new GraphicUi(controller, gamefield)
    loop(controller, textUi)
  }
}