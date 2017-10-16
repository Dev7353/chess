package chess

object chess_app{
  def main(args: Array[String]): Unit ={
    val game = new Chess()
    game.loop()
  }
}