package Part2.ParallelProcessing

object Main {

  def main(args: Array[String]): Unit = {

    val ints = 1 to 10

    val start = System.currentTimeMillis()
    val seq   = Sequential.sum(ints)
    val end   = System.currentTimeMillis()
    println("/--------------------------------- 1")
    println(start - end + "ms")
    println(seq)
    println()

    val start2 = System.currentTimeMillis()
    val par    = Parallel.sum(ints)
    val end2   = System.currentTimeMillis()
    println("/--------------------------------- 2")
    println(start2 - end2 + "ms")
    println(par)
    println()

    val start3 = System.currentTimeMillis()
    Par.sum(ints)
    val end3   = System.currentTimeMillis()
    println("/--------------------------------- 3")
    println(start3 - end3 + "ms")
    println()

    val start4 = System.currentTimeMillis()
    Par.sum2(ints)
    val end4   = System.currentTimeMillis()
    println("/--------------------------------- 4")
    println(start4 - end4 + "ms")
    println()

    val start5 = System.currentTimeMillis()
    Par.par(ints)
    val end5   = System.currentTimeMillis()
    println("/--------------------------------- 5")
    println(start5 - end5 + "ms")
    println()

    println(Par.str == Par.str2) // どっちも逐次（書籍には、sum の実装で並列になるとか言ってる）
    println(Par.str == Par.str3) // Future で実装したものは並列処理になっている
  }
}
