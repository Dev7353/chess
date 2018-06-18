package controller

import javax.swing.JButton
import state._
import model._

import util.control.Breaks._
import scala.collection.mutable.ListBuffer
import scala.concurrent.{Await, Future}
import scala.concurrent.duration.Duration
import scala.concurrent.ExecutionContext.Implicits.global
class Controller(){
  var source: Tuple2[Int, Int] = _
  var target: Tuple2[Int, Int] = _
  var round: Int = 1
  var remoteButton: JButton=_
  var currentPlayer: Player= _
  var enemyPlayer: Player = _
  var playerA: Player = _
  var playerB: Player = _
  var gamefield = new GameField()
  var finish: Boolean = false
  initializeField()

  def initializeField(): Unit ={
    for{
      x <- 2 to 5
      y <- 0 to 7
    }yield gamefield.set((x,y), new Figure)

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
      gamefield.set((1,p), new Bauer((1,p), "UP"))
    }

    gamefield.set((7,0), new Turm())
    gamefield.set((7,7), new Turm())
    gamefield.set((7,1), new Läufer())
    gamefield.set((7,6), new Läufer())
    gamefield.set((7,2), new Offizier())
    gamefield.set((7,5), new Offizier())
    gamefield.set((7,3), new König())
    gamefield.set((7,4), new Dame())
    for( p <- 0 to 7){
      gamefield.set((6,p), new Bauer((6,p), "DOWN"))
    }
  }

  def setPlayerA(player:Player): Unit ={
   playerA = player
    currentPlayer = playerA

    for{
      i <- 0 to 1
      j <- 0 to 7
    } playerA.addFigure(gamefield.get((i,j)))
  }

  def setPlayerB(player:Player): Unit ={
    playerB = player
    enemyPlayer = playerB
    for{
      i <- 6 to 7
      j <- 0 to 7
    } playerB.addFigure(gamefield.get((i,j)))

  }


  def switchPlayers(): Unit ={

    var z = currentPlayer
    currentPlayer = enemyPlayer
    enemyPlayer = z
  }
  def putFigureTo(source: Tuple2[Int, Int], target: Tuple2[Int,Int]): ChessException ={
    putFigureTo(source, target, sameRound = false)
  }
  def putFigureTo(source: Tuple2[Int, Int], target: Tuple2[Int,Int], sameRound: Boolean = false): ChessException ={
    var current_figure = gamefield.get(source)
    if(!currentPlayer.hasFigure(current_figure)){
      return new NoFigureException()
    }

    if(!moveIsAllowed(source, target)){
      return new NoAllowedMoveException()
    }

    val ocupada: Future[Boolean] = Future(gamefield.isOccupied(target))

    if(Await.result(ocupada, Duration.Inf)){
      if(currentPlayer.hasFigure(gamefield.get(target))){
        return new OwnTargetException()
      }

      val deadFigure = gamefield.update(source, target)
      currentPlayer.killed(deadFigure) //add dead enemy figure to current players list
    } else{
      gamefield.update(source, target)
    }

    this.source = source
    this.target = target

    if(!sameRound) {
      //if you did i.e roschade there are 2 putInFigures calls.
      this.round += 1
      setNextPlayer()
    }
    new SuccessDraw()
  }


  def moveIsAllowed(source: Tuple2[Int, Int], target: Tuple2[Int, Int]): Boolean ={
    var möglicheZüge:ListBuffer[Tuple2[Int, Int]] = getPossibleMoves(source)
    möglicheZüge.contains(target)
  }

 def setNextPlayer(): Unit ={
   if(currentPlayer.equals(playerA)){
     enemyPlayer = playerA
     currentPlayer = playerB

   }else {
     enemyPlayer = playerB
     currentPlayer = playerA
   }
 }

  def getFigure(coord: Tuple2[Int, Int]): Figure ={

    gamefield.get(coord)
  }

  def getPossibleMoves(source: Tuple2[Int, Int]): ListBuffer[Tuple2[Int, Int]] ={
    var möglicheZüge = ListBuffer.empty[Tuple2[Int, Int]]
    val source_figure = gamefield.get(source)
    source_figure match {
      case b: Bauer =>
        if(b.getDirection() == "UP"){
          val up = (source._1+1, source._2)
          val left = (source._1+1, source._2-1)
          val right = (source._1+1, source._2+1)
          val firstDraw = (source._1+2, source._2)

         checkBauerZuege(möglicheZüge, up, right, left)

          if(source._1 == 1 && !currentPlayer.hasFigure(gamefield.get(firstDraw)))
            möglicheZüge.+=:(firstDraw)

        }else if(b.getDirection() == "DOWN"){
          println("bauer gets down")
          println(this.currentPlayer)
          println(this.enemyPlayer)
          val up = (source._1-1, source._2)
          val left = (source._1-1, source._2-1)
          val right = (source._1-1, source._2+1)
          val firstDraw = (source._1-2, source._2)

          checkBauerZuege(möglicheZüge, up, right, left)

          if(source._1 == 6 && !currentPlayer.hasFigure(gamefield.get(firstDraw)))
            möglicheZüge.+=:(firstDraw)
        }
      case t: Turm =>
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


      case l: Läufer =>
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
          (source._1+2, source._2+1)
        )

        for(tuple <- coordinates){
          if ((tuple._1 >= 0 && tuple._1 <= 7) && tuple._2 >= 0 && tuple._2 <= 7) {
            if((gamefield.isOccupied(tuple) && !currentPlayer.hasFigure(gamefield.get(tuple))) || !gamefield.isOccupied(tuple))
              möglicheZüge.+=:(tuple)
          }


        }
      case o: Offizier =>
        breakable{
          for(i <- 1 to 7){
            val t = (source._1+i, source._2+i)
            if(t._1 > 7||t._1 < 0||t._2 > 7||t._2 < 0)
              break()
            if(!gamefield.isOccupied(t))
              möglicheZüge+=(t)
            else if(enemyPlayer.hasFigure(gamefield.get(t))){
              möglicheZüge.+=(t)
              break()
            }else
              break()
          }
        }

        breakable{
          for(i <- 1 to 7){
            val t = (source._1+i, source._2-i)
            if(t._1 > 7||t._1 < 0||t._2 > 7||t._2 < 0)
              break()
            if(!gamefield.isOccupied(t))
              möglicheZüge+=(t)
            else if(enemyPlayer.hasFigure(gamefield.get(t))){
              möglicheZüge.+=(t)
              break()
            }else
              break()
          }
        }

        breakable{
          for(i <- 1 to 7){
            val t = (source._1-i, source._2+i)
            if(t._1 > 7||t._1 < 0||t._2 > 7||t._2 < 0)
              break()
            if(!gamefield.isOccupied(t))
              möglicheZüge+=(t)
            else if(enemyPlayer.hasFigure(gamefield.get(t))){
              möglicheZüge.+=(t)
              break()
            }else
              break()
          }
        }

        breakable{
          for(i <- 1 to 7){
            val t = (source._1-i, source._2-i)
            if(t._1 > 7||t._1 < 0||t._2 > 7||t._2 < 0)
              break()
            if(!gamefield.isOccupied(t))
              möglicheZüge+=(t)
            else if(enemyPlayer.hasFigure(gamefield.get(t))){
              möglicheZüge.+=(t)
              break()
            }else
              break()
          }
        }

      case d: Dame => println("Du wählst die Dame!")
      case k: König => println("Du wählst den König!")
        //prüfe falls Zug Roschade ist
        if(source == (0,3) && target == (0,1)){
          if(!gamefield.isOccupied((0,1)) && !gamefield.isOccupied((0,2)) && gamefield.get((0,0)).isInstanceOf[Turm]){
            möglicheZüge.+=:(target)
            putFigureTo((0,0), (0,2), sameRound = true)
          }

        } else if(source == (0,3) && target == (0,6)){
          if(!gamefield.isOccupied((0,5)) && !gamefield.isOccupied((0,6)) && gamefield.get((0,7)).isInstanceOf[Turm]){
            möglicheZüge.+=:(target)
            putFigureTo((0,7), (0,5), sameRound = true)
          }
        }
        else if(source == (7,3) && target == (7,6)){
          if(!gamefield.isOccupied((7,5)) && !gamefield.isOccupied((7,6)) && gamefield.get((7,7)).isInstanceOf[Turm]){
            möglicheZüge.+=:(target)
            putFigureTo((7,7), (7,5), sameRound = true)
          }
        }
        else if(source == (7,3) && target == (7,1)){
          if(!gamefield.isOccupied((7,1)) && !gamefield.isOccupied((7,2)) && gamefield.get((7,0)).isInstanceOf[Turm]){
            möglicheZüge.+=:(target)
            putFigureTo((7,0), (7,2), sameRound = true)
          }
        }
        else{
          if(Math.abs(target._1 -source._1) == 1||Math.abs(target._2 -source._2) == 1){
            möglicheZüge.+=(target)
          }
        }


      case _ => null
    }

    möglicheZüge
  }

  def checkBauerZuege(möglicheZüge: ListBuffer[Tuple2[Int, Int]], up: Tuple2[Int, Int], right: Tuple2[Int, Int], left: Tuple2[Int, Int]):Unit={
    if((up._1 >= 0 && up._1 < 8) && (up._2 >=0 && up._2 < 8) && !currentPlayer.hasFigure(gamefield.get(up)))
      möglicheZüge.+=(up)
    if((left._1 >= 0 && left._1 < 8) && (left._2 >=0 && left._2 < 8) &&enemyPlayer.hasFigure(gamefield.get(left)))
      möglicheZüge.+=(left)
    if((right._1 >= 0 && right._1 < 8) && (right._2 >=0 && right._2 < 8) &&enemyPlayer.hasFigure(gamefield.get(right)))
      möglicheZüge.+=(right)
  }
}
