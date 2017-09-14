package model

class GameField {

  var field = Array.ofDim[Figure](8,8)

  def set(pos: Tuple2[Int, Int], figure: Figure): Unit ={

      field(pos._1)(pos._2) = figure
  }

  def isOccupied(spot: Tuple2[Int, Int]): Boolean={
    if(field(spot._1)(spot._2).isInstanceOf[Figure])
      false
    else
      true
  }

  def update(src: Tuple2[Int, Int],  tar: Tuple2[Int, Int]): Figure ={
    val ret = field(tar._1)(tar._2)
    field(tar._1)(tar._2) = field(src._1)(src._2)
    field(src._1)(src._2) =  new Figure

    ret
  }

  def get(pos: Tuple2[Int, Int]): Figure={
    field(pos._1)(pos._2)
  }

  override def toString():String ={

    var sb: StringBuilder =  new StringBuilder()

    for(x <- 0 to 7){
      for(y <- 0 to 7){
        sb.append(field(x)(y).toString + " \t")
      }
      sb.append(x.toString() + "\n")
    }

    sb.toString()
  }

}
