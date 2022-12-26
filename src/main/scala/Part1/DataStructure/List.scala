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
