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

    val login = new Thread{
      var flagA = false
      var terminate = false
      override def run(): Unit = {

        val playerA = readLine("Player A: ")
        breakable {
          while (!flagA) {
            if (terminate)
              break()
          }
          val playerB = readLine("Player B: ")

          controller.setPlayerA(new Player(playerA))
          controller.setPlayerB(new Player(playerB))
          controller.performClick()
        }

      }

      override def destroy(): Unit = {

        terminate = true
      }
    }

    login.start()

    while(controller.playerA == null && controller.playerB == null){}
    login.destroy()
    textUi.draw()

    while (true){
      println("----- ROUND "+controller.round+" -----")
      println("\n Select Figure\n")
      var x, y = scala.io.StdIn.readLine()
      println("\n Select Target position\n")
      var t_x, t_y = scala.io.StdIn.readLine()


      var ret = controller.putFigureTo((x.toInt,y.toInt), (t_x.toInt, t_y.toInt))
      if(ret.isInstanceOf[NoFigureException]){
        println("You don't have such a figure!")
      } else if(ret.isInstanceOf[NoAllowedMoveException])
        println("You're not supposed to make this move!")
      else if(ret.isInstanceOf[OwnTargetException])
        println("You cannot kill your own figures!")
      else{

        println("put " + (x,y).toString() + " to " + (t_x, t_y).toString())
        //controller.setNextPlayer()
        println("Next Round. Player ist " + controller.currentPlayer)
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