package model

class Bauer(kord: Tuple2[Int, Int]) extends Figure{

  var this.kord = kord

  override def toString(): String={

    return "B"
  }

  def getKord(): Tuple2[Int, Int]={
    this.kord
  }

}
