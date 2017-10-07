package model

class Bauer(kord: Tuple2[Int, Int], direction: String) extends Figure{

  var this.kord = kord
  var this.direction = direction
  override def toString(): String={

    "B"
  }

  def getKord(): Tuple2[Int, Int]={
    this.kord
  }

  def getDirection(): String={

    this.direction
  }

}
