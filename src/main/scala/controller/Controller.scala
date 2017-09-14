package controller

import state.{ChessException, NoFigureException, SuccessDraw}
import model._
import observer.Observable

class Controller(playerA: Player, playerB: Player, gamefield: GameField) extends Observable{

  var currentPlayer = playerA
  initializeField()

  def initializeField(): Unit ={
    gamefield.set((0,0), new Turm())
    gamefield.set((0,7), new Turm())
    gamefield.set((0,1), new Läufer())
    gamefield.set((0,6), new Läufer())
    gamefield.set((0,2), new Offizier())
    gamefield.set((0,5), new Offizier())
    gamefield.set((0,3), new König())
    gamefield.set((0,4), new Dame())
    for( p <- 0 to 7){
      gamefield.set((1,p), new Bauer())
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
      gamefield.set((6,p), new Bauer())
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


    for(x <- 2 to 5){
      for(y <- 0 to 7){
        gamefield.set((x,y), new Figure)
      }
    }
  }

  def putFigureTo(source: Tuple2[Int, Int], target: Tuple2[Int,Int]): ChessException ={
    if(!currentPlayer.hasFigure(gamefield.get(source))){
      new NoFigureException()
    }

   /* if(!moveIsAllowed()){
      NoAllowedMoveException()
    }*/

    if(gamefield.isOccupied(target)){

      val deadFigure = gamefield.update(source, target)
      currentPlayer.killed(deadFigure) //add dead enemy figure to current players list
    } else{
      gamefield.update(source, target)
    }

    notifyObservers
    print("notify observers")
    new SuccessDraw()
  }

  def setNextPlayer(): Unit ={
    if(currentPlayer == playerA)
      currentPlayer = playerB
    else
      currentPlayer = playerA
  }
}
