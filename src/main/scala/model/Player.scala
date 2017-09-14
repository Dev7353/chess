package model
import java.util.LinkedList

class Player(name:String) {

  var figures:LinkedList[Figure] = new java.util.LinkedList[Figure]()
  var killed:LinkedList[Figure] = new java.util.LinkedList[Figure]()
  def addFigure(figure:Figure): Unit ={
    figures.add(figure)
  }

  def killed(figure: Figure): Unit ={
    killed.add(figure)
  }

  def hasFigure(figure: Figure): Boolean={

    figures.contains(figure)
  }

  override def toString: String = {

    name
  }

}
