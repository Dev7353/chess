package model
import scala.collection.mutable.ListBuffer

class Player(name:String) {

  //var figures:LinkedList[Figure] = new java.util.LinkedList[Figure]()
  var figures = ListBuffer.empty[Figure]
  var killed = ListBuffer.empty[Figure]
  def addFigure(figure:Figure): Unit ={
    figures.+=(figure)
  }

  def killed(figure: Figure): Unit ={
    figures.+=(figure)
  }

  def hasFigure(figure: Figure): Boolean={
    for (f <- figures){
      if(f eq figure) {
        return true
      }
    }
    false
  }

  override def toString: String = {

    name
  }

}
