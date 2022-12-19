package Part1.Foundation

import scala.collection.StringOps

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
