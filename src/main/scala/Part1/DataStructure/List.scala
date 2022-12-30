package Part1.DataStructure

// 共変にすることで Nothing や、Enum などを許容する
sealed trait List[+A]

final case object Nil extends List[Nothing]

final case class VList[A](head: A, tail: List[A]) extends List[A]

object List {

  def apply[A](list: A*): List[A] =
    if (list.isEmpty) Nil else VList(list.head: A, apply[A](list.tail: _*))

  def empty: Nil.type = Nil

  /**
   * 練習問題1
   * 最初の要素を削除する関数 dropHead を実装せよ
   */
  def dropHead[A](list: List[A]): List[A] =
    list match {
      case VList(_, tail) => tail
      case Nil            => Nil
    }

  /**
   * 練習問題2
   * 最初の要素を違う値にする関数 setHead を実装せよ
   */
  def setHead[A](list: List[A], f: A => A): List[A] =
    list match {
      case VList(head, tail) => VList(f(head), tail)
      case Nil               => Nil
    }

  /**
   * 練習問題3
   * 任意の数を指定し、先頭 n 個を削除する関数 drop を実装せよ
   */
  def drop[A](list: List[A], n: Int): List[A] = {
    def loop(list: List[A], n: Int): List[A] =
      (list, n == 0) match {
        case (VList(head, tail), false) => loop(tail, n - 1)
        case (Nil,               true)  => Nil
        case (v@VList(_, _),     true)  => v
      }
    loop(list, n)
  }

  /**
   * 練習問題4
   * 述語にマッチする場合に限り、List からその要素までの要素を削除する dropWhile を実装せよ
   */
  def dropWhile[A](vList: List[A], f: A => Boolean): List[A] = {
    def loop(list: List[A], flag: Boolean): List[A] = {
      (list, flag) match {
        case (VList(head, tail), false) =>
          val matches = f(head)
          if (matches) tail else loop(tail, matches)
        case (Nil, false)  => vList
        case _             => Nil
      }
    }

    loop(vList, false)
  }

  /**
   * 練習問題5
   * 末尾の値を除いて返す関数 init は、一定時間で実行できない
   * それはなぜか
   */
   def init[A](list: List[A]): List[A] = ???
  // 単方向 List の構造上、先頭からしか値を参照できないため、実行時間は List の長さに依存する

  def sum(ints: List[Int]): Int =
    ints match {
      case Nil         => 0
      case VList(h, t) => h + sum(t)
    }

  def product(ds: List[Double]): Double =
    ds match {
      case Nil         => 1.0
      case VList(h, t) => h * product(t)
    }

  /**
   * 上記のような sum, product があった場合、重複している部分を関数の引数として抽出すると、それらを一般化できる
   */
  def foldRight[A, B](list: List[A], init: B)(f: (A, B) => B): B = {
    list match {
      case Nil                           => init
      case VList(head: A, tail: List[A]) => f(head, foldRight(tail, init)(f))
    }
  }

  def sum2(ints: List[Int]): Int =
    foldRight(ints, 0)(_ + _)

  def product2(ds: List[Double]): Double =
    foldRight(ds, 1.0)(_ * _)

  /**
   * 問6: product2 は 0.0 を検出した場合に、直ちに再帰を中止して 0.0 を返せるか
   * 答:
   * 可能であると思う
   * foldRight を実行する前に、0.0 が含まれているかチェックする処理を入れれば良い
   * contains みたいなのを実装すれば、事前にチェックできる
   * foldRight のみを使用して再起を中止することはできない
   */

  def map[A, B](list: List[A], f: A => B): List[B] = {
    var v: Seq[B] = Seq.empty[B]

    def loop(list: List[A]): List[B] = {
      list match {
        case VList(head: A, tail: List[A]) =>
          v = v :+ f(head)
          loop(tail)
        case Nil =>
          List.apply(v: _*)
      }
    }
    loop(list)
  }

  def filter[A](list: List[A], f: A => Boolean): List[A] = {
    var v = Seq.empty[A]
    def loop(list: List[A]): List[A] = {
      list match {
        case VList(head: A, tail: List[A]) =>
          f(head) match {
            case true =>
              v = v :+ head
              loop(tail)
            case false =>
              loop(tail)
          }
        case Nil =>
          List.apply(v: _*)
      }
    }
    loop(list)
  }
}
