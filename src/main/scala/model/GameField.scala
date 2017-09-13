package model

class GameField {

  var field = Array.ofDim[Figure](8,8)

  def set(pos: Tuple2[Int, Int], figure: Figure): Unit ={

      field(pos._1)(pos._2) = figure
  }

  override def toString():String ={

    var sb: StringBuilder =  new StringBuilder()

    for(x <- 0 to 7){
      for(y <- 0 to 7){
        sb.append(field(x)(y).toString + "\t")
      }
      sb.append("\n")
    }

    sb.toString()
  }

}
