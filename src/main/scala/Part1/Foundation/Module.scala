package Part1.Foundation

object Module {

  // 絶対値を取得する
  def absolute(num: Int): Int = num.abs

  // 指定した n 番目のフィボナッチ数を取得する
  def fibonacci(num: Int): Int = {
    if (num <= 0) return 0
    def loop(num: Int, prev: Int = 0, current: Int = 1): Int = {
      num <= 0 match {
        case true  => current
        case false =>
          val next = prev + current
          loop(num - 1, current, next)
      }
    }
    loop(num)
  }

  def calcResult(log: String, func: Int => Int, num: Int): String = {
    val result: Int = func(num)
    s"""
      |++++++++++++++++++++++++++++++
      |$log
      |
      |Result:  $result
      |++++++++++++++++++++++++++++++
      |""".stripMargin
  }
}
