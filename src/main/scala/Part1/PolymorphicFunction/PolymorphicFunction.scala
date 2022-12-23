package Part1.PolymorphicFunction

object PolymorphicFunction {

  def main(args: Array[String]): Unit = {

    val strArray: Array[String] = Array(
      "Functional Programming",
      "Object Oriented Programming",
      "Procedural Programming"
    )

    val str: String = "Functional Programming"
    val str2: String = "関数型プログラミング"

    println(findForStringType(strArray, str)) // Some(Functional Programming)
    println(findForStringType(strArray, str2)) // None

    // ----------------------------------------------------

    val intArray: Array[Int] = Array(
      1, 2, 3, 4, 5
    )

    val intCheckForTwo: Int => Boolean = (int: Int) => int == 2

    // ----------------------------------------------------

    case class T(v: String)
    val tArray: Array[T] = Array(
      T("A"),
      T("B"),
      T("C"),
      T("D"),
      T("E")
    )

    val tCheckForD: T => Boolean = (t: T) => t equals T("D")
    val tCheckForF: T => Boolean = (t: T) => t equals T("F")

    // ----------------------------------------------------

    println(find(intArray, intCheckForTwo)) // Some(2)

    println(find(tArray, tCheckForD)) // Some(T(D))
    println(find(tArray, tCheckForF)) // None

    // ----------------------------------------------------

    val ascSort: (Int, Int) => Boolean = (int, int2) => int < int2

    val intArray2: Array[Int] = Array(
      10, 3, 12, 20202, 222, 1
    )

    println(isSorted(intArray, ascSort)) // true
    println(isSorted(intArray2, ascSort)) // false

  }

  /**
   * 単相関数
   * str で指定した文字列と等価である最初の値を Some(_) で返す
   * 等価の値が見つからない場合は、None を返す
   */
  private def findForStringType(array: Array[String], str: String): Option[String] = {
    def loop(n: Int): Option[String] = {
      if (array.length <= n) None
      else if (array(n) == str) Some(array(n))
      else loop(n.+(1))
    }

    loop(0)
  }

  /**
   * 多相関数
   * A 型として抽象的に定義することで、汎用的に使える関数になる
   */
  private def find[A](array: Array[A], f: A => Boolean): Option[A] = {
    def loop(n: Int): Option[A] = {
      if (array.length <= n) None
      else if (f(array(n))) Some(array(n))
      else loop(n.+(1))
    }

    loop(0)
  }

  /**
   * 練習問題
   * 指定された比較関数に従って Array[A] がソートされているかどうか調べる isSorted を実装せよ
   * わからんくてパクリ：https://github.com/cmeiklejohn/fpinscala/blob/master/IsSorted.scala
   */
  private def isSorted[A](array: Array[A], ordered: (A, A) => Boolean): Boolean = {
    def loop(n: Int, acc: Boolean): Boolean = {
      if ((n + 1) >= array.length)
        acc
      else
        loop(n + 1, acc && ordered(array(n), array(n + 1)))
    }

    loop(0, true)
  }
}
