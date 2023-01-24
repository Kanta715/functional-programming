package Part2.ParallelProcessing

object Main {

  def main(args: Array[String]): Unit = {

    val ints = 1 to 10000000

    val start = System.currentTimeMillis()
    val seq   = Sequential.sum(ints)
    val end   = System.currentTimeMillis()
    println("/-- 逐次処理 -----------------------------")
    println(start - end + "ms")
    println(seq)
    println()

    val start2 = System.currentTimeMillis()
    val par    = Parallel.sum(ints)
    val end2   = System.currentTimeMillis()
    println("/-- 並列処理 -----------------------------")
    println(start2 - end2 + "ms")
    println(par)
    println()

    // /-- 逐次処理 -----------------------------
    // -96ms
    // -2004260032
    //
    // /-- 並列処理 -----------------------------
    // -282ms
    // -2004260032

    val start3 = System.currentTimeMillis()
    val p      = Par.sum(ints)
    val end3   = System.currentTimeMillis()
    println("/-- 並列処理 -----------------------------")
    println(start3 - end3 + "ms")
    println(p)

    // /-- 並列処理 -----------------------------
    // -426ms
    // -2004260032
  }
}
