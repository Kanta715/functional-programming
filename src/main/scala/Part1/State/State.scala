package Part1.State

object State {

  def main(args: Array[String]): Unit = {
    // 副作用を持つ
    // 内部状態が常に変化している
    val random = scala.util.Random
    println(random.nextInt)
    println(random.nextInt)

    println()
    // 本に書かれていたサンプル（よくわからん）
    val sRandom = SimpleRNG(100)
    println(sRandom.nextInt._1)
    println(sRandom.nextInt._1)

    println()

  }
}
