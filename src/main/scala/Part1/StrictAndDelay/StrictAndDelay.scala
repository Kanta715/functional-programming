package Part1.StrictAndDelay

object StrictAndDelay {

  def main(args: Array[String]): Unit = {

    // map, filter などの純粋関数を都度実行すると、先頭から全ての値を走査し、新しい List を返す
    // 以下の例だと単純な計算だが、3度も走査し、List を生成している
    val list: List[Int] = List(1, 2, 3, 4, 5, 6).map(_ + 10).filter(_ % 2 == 0).map(_ * 3)
    println(list)  // List(36, 42, 48)

    /*
      細かく分けると以下のように中間のリストを生成している
      List(1, 2, 3, 4, 5, 6).map(_ + 10).filter(_ % 2 == 0).map(_ * 3)
      List(11, 12, 13, 14, 15, 16).filter(_ % 2 == 0).map(_ * 3)
      List(12, 14, 16).map(_ * 3)
      List(36, 42, 48)
     */

    false && {
      println("評価")
      true
    } // 何も出力しない

    true || {
      println("評価")
      false
    } // 何も出力しない

    if (list.isEmpty) sys.error("評価されました！！！") else list // エラーは評価されていない

    def if2[A](cond: Boolean, onTrue: () => A, onFalse: () => A): A =
      if (cond) onTrue() else onFalse()

    if2(true, () => println("true!!!"), () => sys.error("false!!!")) // true!!!

    def if3[A](cond: Boolean, onTrue: => A, onFalse: => A): A =
      if (cond) onTrue else onFalse

    if3(true, println("TRUE!!!"), sys.error("FALSE!!!")) // TRUE!!!

    def notCache(b: Boolean, i: => Int): Int = if (b) i + i else 0

    notCache(true, {
      println("Not cache !!!!!")
      100
    })
    // Not cache !!!!!
    // Not cache !!!!!

    def cache(b: Boolean, i: => Int): Int = {
      lazy val j = i
      if (b) j + j else 0
    }

    cache(true, {
      println("Cache !!!!!")
      100
    })
    // Cache !!!!!
  }
}
