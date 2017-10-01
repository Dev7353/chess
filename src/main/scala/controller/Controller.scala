package controller

import state._
import model._
import observer.Observable
import util.control.Breaks._

import scala.collection.mutable.ListBuffer

class Controller(playerA: Player, playerB: Player, gamefield: GameField) extends Observable{

  var currentPlayer = playerA
  var enemyPlayer = playerB
  initializeField()

  def initializeField(): Unit ={
    for(x <- 2 to 5){
      for(y <- 0 to 7){
        gamefield.set((x,y), new Figure)
      }
    }
    gamefield.set((0,0), new Turm())
    gamefield.set((0,7), new Turm())
    gamefield.set((0,1), new Läufer())
    gamefield.set((0,6), new Läufer())
    gamefield.set((0,2), new Offizier())
    gamefield.set((0,5), new Offizier())
    gamefield.set((0,3), new König())
    gamefield.set((0,4), new Dame())
    // end
    for( p <- 0 to 7){
      gamefield.set((1,p), new Bauer((1,p)))
    }

    playerA.addFigure(gamefield.get((0,0)))
    playerA.addFigure(gamefield.get((0,7)))
    playerA.addFigure(gamefield.get((0,1)))
    playerA.addFigure(gamefield.get((0,6)))
    playerA.addFigure(gamefield.get((0,2)))
    playerA.addFigure(gamefield.get((0,5)))
    playerA.addFigure(gamefield.get((0,3)))
    playerA.addFigure(gamefield.get((0,4)))
    playerA.addFigure(gamefield.get((1,0)))
    playerA.addFigure(gamefield.get((1,1)))
    playerA.addFigure(gamefield.get((1,2)))
    playerA.addFigure(gamefield.get((1,3)))
    playerA.addFigure(gamefield.get((1,4)))
    playerA.addFigure(gamefield.get((1,5)))
    playerA.addFigure(gamefield.get((1,6)))
    playerA.addFigure(gamefield.get((1,7)))


    gamefield.set((7,0), new Turm())
    gamefield.set((7,7), new Turm())
    gamefield.set((7,1), new Läufer())
    gamefield.set((7,6), new Läufer())
    gamefield.set((7,2), new Offizier())
    gamefield.set((7,5), new Offizier())
    gamefield.set((7,3), new König())
    gamefield.set((7,4), new Dame())
    for( p <- 0 to 7){
      gamefield.set((6,p), new Bauer((6,p)))
    }

    playerB.addFigure(gamefield.get((7,0)))
    playerB.addFigure(gamefield.get((7,7)))
    playerB.addFigure(gamefield.get((7,1)))
    playerB.addFigure(gamefield.get((7,6)))
    playerB.addFigure(gamefield.get((7,2)))
    playerB.addFigure(gamefield.get((7,5)))
    playerB.addFigure(gamefield.get((7,3)))
    playerB.addFigure(gamefield.get((7,4)))
    playerB.addFigure(gamefield.get((6,0)))
    playerB.addFigure(gamefield.get((6,1)))
    playerB.addFigure(gamefield.get((6,2)))
    playerB.addFigure(gamefield.get((6,3)))
    playerB.addFigure(gamefield.get((6,4)))
    playerB.addFigure(gamefield.get((6,5)))
    playerB.addFigure(gamefield.get((6,6)))
    playerB.addFigure(gamefield.get((6,7)))



  }

  def putFigureTo(source: Tuple2[Int, Int], target: Tuple2[Int,Int]): ChessException ={

    var current_figure = gamefield.get(source)
    if(!currentPlayer.hasFigure(current_figure)){
      return new NoFigureException()
    }

    if(!moveIsAllowed(source, target)){
      return new NoAllowedMoveException()
    }

    if(gamefield.isOccupied(target)){
      if(currentPlayer.hasFigure(gamefield.get(target))){
        return new OwnTargetException()
      }

      val deadFigure = gamefield.update(source, target)
      currentPlayer.killed(deadFigure) //add dead enemy figure to current players list
    } else{
      gamefield.update(source, target)
    }
    notifyObservers
    new SuccessDraw()
  }

  def setNextPlayer(): Unit ={
    if(currentPlayer == playerA)
      currentPlayer = playerB
    else
      currentPlayer = playerA
  }


  def moveIsAllowed(source: Tuple2[Int, Int], target: Tuple2[Int, Int]): Boolean ={
    var source_figure = gamefield.get(source)
    var möglicheZüge = ListBuffer.empty[Tuple2[Int, Int]]
    source_figure match {
      case b: Bauer => println("Du wählst einen Bauer du Bauer!")
        return true
      case t: Turm => println("Du wählst einen Turm!")
        //wenn Ziel nicht mit x oder y Wert übereinstimmt, kann nicht horizontal bzw vertikal gelaufen werden
        if(source._1 == target._1 || source._2 == target._2){


          //horizontal
          breakable {
            for (n <- source._2+1 to 7) {
              if (gamefield.isOccupied((source._1, n)) && currentPlayer.hasFigure(gamefield.get(target)))
                break()
              else if(gamefield.isOccupied((source._1, n)) && !currentPlayer.hasFigure(gamefield.get(target))){
                möglicheZüge.+=((source._1, n))
                break()
              } else
                möglicheZüge.+=((source._1, n))

            }
          }
          breakable {
            for (n <- source._2-1 to 0 by -1) {
              if (gamefield.isOccupied((source._1, n)) && currentPlayer.hasFigure(gamefield.get(target)))
                break()
              else if(gamefield.isOccupied((source._1, n)) && !currentPlayer.hasFigure(gamefield.get(target))){
                möglicheZüge.+=((source._1, n))
                break()
              } else
                möglicheZüge.+=((source._1, n))

            }
          }

          //vertikal
          breakable {
            for (n <- source._1+1 to 7) {
              if (gamefield.isOccupied((n, source._2)) && currentPlayer.hasFigure(gamefield.get(target)))
                break()
              else if(gamefield.isOccupied((n, source._2)) && !currentPlayer.hasFigure(gamefield.get(target))){
                möglicheZüge.+=((n, source._2))
                break()
              } else
                möglicheZüge.+=((n, source._2))

            }
          }
          breakable {
            for (n <- source._1-1 to 0 by -1) {
              if (gamefield.isOccupied((n, source._2)) && currentPlayer.hasFigure(gamefield.get(target)))
                break()
              else if(gamefield.isOccupied((n, source._2)) && !currentPlayer.hasFigure(gamefield.get(target))){
                möglicheZüge.+=((n, source._2))
                break()
              } else
                möglicheZüge.+=((n, source._2))

            }
          }

          print("[Turm] Mögliche Züge " + möglicheZüge)
          if(möglicheZüge.contains(target))
            return true
          else return false
        }
        else return false

      case l: Läufer => println("Du wählst einen Läufer!")
        var oben: Tuple2[Int, Int] = (source._1-1, source._2+2)
        var unten: Tuple2[Int, Int] = (source._1+1, source._2+2)
        möglicheZüge.+=:(oben).+=:(unten)

        val coordinates  = List[Tuple2[Int, Int]](
          (source._1-2, source._2-1),
          (source._1-2, source._2+1),
          (source._1-1, source._2-2),
          (source._1-1, source._2+2),
          (source._1+1, source._2-2),
          (source._1+1, source._2+2),
          (source._1+2, source._2-1),
          (source._1+2, source._2+1),
        )

        for(tuple <- coordinates){
          println("[debug]" + tuple)

          if ((tuple._1 >= 0 && tuple._1 <= 7) && ((tuple._2 >= 0 && tuple._2 <= 7))) {
            if((gamefield.isOccupied(tuple) && !currentPlayer.hasFigure(gamefield.get(tuple))) || !gamefield.isOccupied(tuple))
              möglicheZüge.+=:(tuple)
          }


        }


        return möglicheZüge.contains(target)
      case o: Offizier => println("Du wählst einen Offizier!")
        return true
      case d: Dame => println("Du wählst die Dame!")
        return true
      case k: König => println("Du wählst den König!")
        return true

      case _ => return false
    }


  }
}
