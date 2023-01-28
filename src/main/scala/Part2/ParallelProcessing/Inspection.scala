package Part2.ParallelProcessing

import scala.collection.parallel.ParSeq

// 書籍でかいてあることがよくわからんから別途の検証実行用のオブジェクト
object Inspection {

  def main(args: Array[String]): Unit = {

    val ints = 1 to 10000

    val start  = System.currentTimeMillis()
    sum(ints)
    val end    = System.currentTimeMillis()
    println("/ ----------------------------- 並列処理：書籍")
    println(i)
    println(start - end + "ms")

    val start2  = System.currentTimeMillis()
    parallel(ints)
    val end2    = System.currentTimeMillis()
    println("/ ----------------------------- 並列処理：標準ライブラリ")
    println(n)
    println(start2 - end2 + "ms")

    // / ----------------------------- 並列処理：書籍
    // 12345678910111213141516171819202122232425....: 普通に逐次処理
    // 292ms
    // / ----------------------------- 並列処理：標準ライブラリ
    // 25012500231543162504678375775081037592509....: 並列処理だから可変の変数に対して処理を行うとこうなることは理解できる
    // 88ms:                                          処理速度は並列の分速い（問題は書籍に書いてある並列処理がどう考えても逐次処理だと思う）

    // ------------------------------------------------------------------------

  }

  // 書籍で書いていた並列処理ができららしい関数
  var i = ""
  def sum(ints: IndexedSeq[Int]): Int = {
    if (ints.size <= 1) {
      val int = ints.headOption getOrElse 0
      i = i + int
      int
    } else {
      val (l, r) = ints.splitAt(ints.size / 2)
      val sumL: Par[Int] = Par.unit(sum(l))
      val sumR: Par[Int] = Par.unit(sum(r))
      Par.get(sumL) + Par.get(sumR)
    }
  }

  // 並列処理の標準ライブライがあったので使ってみる
  // https://github.com/scala/scala-parallel-collections
  var n = ""
  def parallel(ints: Seq[Int]): Unit = {
    val par = ParSeq.apply(ints:_*)
    par.foreach(int => {
      n = n + int
    })
  }
}
