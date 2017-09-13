package controller

import model._

class Controller(playerA: String, playerB: String, gamefield: GameField){

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

    //debug
    for(x <- 2 to 7){
      for(y <- 0 to 7){
        gamefield.set((x,y), new Figure)
      }
    }

  }
}
