## 1. 関数型プログラミングの基礎

### 1-1. 高階関数：関数に関数を渡す
純粋関数型プログラミングでは、他の関数を引数として受け取る関数を記述すると便利なことがよくあり、高階関数と呼ばれる。
#### 1-1-1. 高階関数の作成
まず、適当に `Int => Int` の関数を作成。

次に引数に関数をとる高階関数を定義してみる。
```Scala
object Module {

  // 絶対値を取得する
  def absolute(num: Int): Int = num.abs

  // 指定した n 番目のフィボナッチ数を取得する
  // 練習問題であったのでやってみた
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

  // 高階関数
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

object Main {

  def main(args: Array[String]): Unit = {
    val funcName: String = args.headOption.getOrElse("fib")
    val num:      Int    = args.toSeq.tail.headOption.flatMap(new StringOps(_).toIntOption).getOrElse(100)

    val result = funcName match {
      case "fib" => Module.calcResult(
        "Fibonacci number",
        Module.fibonacci,
        num
      )
      case "abs" => Module.calcResult(
        "Absolute value",
        Module.absolute,
        num
      )
      case _     => {
        val errMsg =
          s"""
             |😉 引数一覧のいずれかの引数を指定してください（指定しなかった場合はデフォルトで fib, 100 が渡されます）
             |
             |📕 引数一覧
             |  ⭐️ 第一引数
             |  | [ fib ] : n 番目のフィボナッチ数を出力します
             |  | [ abs ] : n の絶対値を出力します
             |
             |  ⭐️ 第二引数
             |  | 数値を指定してください
             |  | 第一引数で指定した関数に渡される数値です
             |  | 半角数字を指定していない場合でもデフォルト値が渡されます
             |
             |""".stripMargin
        throw new IllegalArgumentException(errMsg)
      }
    }
    println(result)
  }
}
```
