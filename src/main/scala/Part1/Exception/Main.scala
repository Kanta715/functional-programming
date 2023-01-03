package Part1.Exception

object Main {

  def main(args: Array[String]): Unit = {
    println(Faile.failing2(100))
    // Faile.failing(100) Error

    val seq: Seq[Double] = Seq(
      1.4,
      55.7,
      333.3,
      22.22,
      222.1,
      1.5
    )
    val empty: Seq[Double] = Seq.empty[Double]

    println(NotOption.mean(seq))
    // Option.mean(empty) Error

    // 呼び出し元がリストが空の場合の対処方法を知っておかなければならず、返される型が Double に限定される
    println(NotOption.mean2(empty, 0.0))

    Option.mean(empty) match {
      case Some(v) => println(v)
      case None    => println("リストの値が見つかりません")
    }

    Option.variance(seq) match {
      case Some(v) => println(v)
      case None    => println("リストが見つかりません")
    }
  }
}
