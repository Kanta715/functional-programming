package Part1.Exception

object Faile {

  val SYSTEM_ERROR_CODE = 500

  def failing(int: Int): Int = {
    val y: Int = throw new Exception("関数の実行に失敗しました")
    try {
      val x: Int = 50 + 50
      x + y
    } catch {
      case e: Exception => SYSTEM_ERROR_CODE
    }
  }

  def failing2(int: Int): Int = {
    try {
      val x: Int = 50 + 50
      x + ((throw new Exception("関数の実行に失敗しました")): Int)
    } catch {
      case e: Exception => SYSTEM_ERROR_CODE
    }
  }
}
