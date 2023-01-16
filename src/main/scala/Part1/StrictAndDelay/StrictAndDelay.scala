package Part1.StrictAndDelay

import scala.util.Random

object StrictAndDelay {

  def main(args: Array[String]): Unit = {

    // map, filter などの純粋関数を都度実行すると、先頭から全ての値を走査し、新しい List を返す
    // 以下の例だと単純な計算だが、3度も走査し、List を生成している
    val list: List[Int] = List(1, 2, 3, 4, 5, 6).map(_ + 10).filter(_ % 2 == 0).map(_ * 3)
    println(list) // List(36, 42, 48)

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

    val double: Int => Int = (x: Int) => {
      println("execute")
      x * 2
    }
    val stream = Stream(10, 100, 1000)
    val cons = Cons(() => double(1), () => stream)
    // 2回 double が呼ばれている
    val head = cons.headOption
    val head2 = cons.headOption
    println(head)
    println(head2)

    val s = Stream.cons(double(1), stream)
    val sHead = s.headOption
    val sHead2 = s.headOption
    println(sHead)
    println(sHead2)
    // execute
    // Some(2)
    // Some(2)

    val cons2 = Cons(() => 2, () => Cons(() => 20, () => Cons(() => 200, () => Cons(() => 2000, () => Cons(() => 20000, () => Stream.empty[Int])))))
    println(cons2)
    println(cons2.toList)

    println()
    // --------------------------------------------------------------
    println("----------------------------------")
    println()
    val took = stream.take(2)
    val took2 = stream.take(100)
    println(took.toList)
    println(took2.toList)
    // List(10, 100)
    // List(10, 100, 1000)

    val dropped = stream.drop(2)
    val dropped2 = stream.drop(100)
    println(dropped.toList)
    println(dropped2.toList)
    // List(1000)
    // List()

    val filtered = stream.takeWhile(_ == 100)
    println(filtered.toList)

    val result = stream.exists(v => {
      println("exists")
      v == 100
    })
    println(result)
    // exists
    // exists
    // true

    import Part1.DataStructure.List
    val f: Int => Boolean = (x: Int) => {
      println("Execute")
      x == 100
    }
    val result2 = List.exists(List.apply(10, 100, 1000), f)
    println(result2)
    // Execute
    // Execute
    // Execute
    // true

    val result3 = cons2.exists(v => {
      println("Cons")
      v == 20
    })
    println(result3)
    // Cons
    // Cons
    // true

    val result4 = cons2.forAll(v => {
      v < 2000
    })
    println(result4)

    val result5 = cons2.headOption2
    println(result5)

    val result6 = cons2.map(v => {
      val random = Random.nextInt(1000)
      println(random)
      random + v
    })
    println(result6.toList)

    val result7 = cons2.filter(v => v == 2000)
    println(result7.toList)

    val result8 = cons2.append(200000)
    println(result8.toList)

    println()
    val result9 = cons2.map(_ + 100).filter(_ != 200).toList
    println(result9)
    // map
    // filter
    // map
    // filter
    // map
    // filter
    // map
    // filter
    // map
    // filter
    // List(102, 120, 300, 2100, 20100)

    println()
    val listResult = List(1, 10, 100, 1000, 10000).mapS(_ + 100).filterS(_ != 100)
    println(listResult)
    // MapS
    // MapS
    // MapS
    // MapS
    // MapS
    // MapS
    // FilterS
    // FilterS
    // FilterS
    // FilterS
    // FilterS
    // FilterS
    // VList(101,VList(110,VList(200,VList(1100,VList(10100,Nil)))))

    println()
    // --------------------------------------------------------------

    val infinite = Stream.constant(100)
    println(infinite.take(10).toList)

    val from = Stream.from(300)
    println(from.take(5).toList)

    val fibs = Stream.fibs
    println(fibs.take(20).toList)

    val unfold = Stream.unfold(1)(x => { if (x <= 100) Some(((println(x), x)._2 ,x + 1)) else None })
    println(unfold.take(200).toList)
  }
}
