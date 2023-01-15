package Part1.StrictAndDelay

trait Stream[+A] {

  def headOption: Option[A] =
    this match {
      case Empty      => None
      case Cons(h, _) => Some(h())
    }

  // Stream の値を全て評価した List を返す関数を実装せよ
  def toList: List[A] = {
    this match {
      case Empty      => Nil
      case Cons(h, t) =>
        def loop(cons: Stream[A], stock: List[A]): List[A] =
          cons match {
            case Empty      => stock
            case Cons(h, t) => loop(t(), stock :+ h())
          }
        loop(t(), List(h()))
    }
  }

  // Stream の先頭から n 個の要素を抜き出す関数 take を実装せよ
  def take(n: Int): Stream[A] = {
    this match {
      case Empty       => Empty
      case c@Cons(_,_) =>
        def loop(stream: Stream[A], stock: List[A], took: Int): List[A] = {
          if (took <= n) {
            stream match {
              case Empty      => stock
              case Cons(h, t) => loop(t(), stock :+ h(), took + 1)
            }
          } else stock
        }
        Stream(loop(c, List(), 1):_*)
    }
  }

  // Stream の先頭から n 個の要素をスキップする関数 drop を実装せよ
  def drop(n: Int): Stream[A] = {
    this match {
      case Empty        => Empty
      case c@Cons(_, _) =>
        def loop(stream: Stream[A], stock: List[A], removed: Int): List[A] =
          stream match {
            case Empty      => stock
            case Cons(h, t) =>
              if (removed <= n) {
                loop(t(), stock, removed + 1)
              } else {
                loop(t(), stock :+ h(), removed + 1)
              }
          }

        Stream(loop(c, List(), 1): _*)
    }
  }

  // Stream の値から指定された述語とマッチする全ての値を取得する takeWhile 関数を実装せよ
  def takeWhile(f: A => Boolean): Stream[A] =
    this match {
      case Empty        => Empty
      case c@Cons(_, _) =>
        def loop(stream: Stream[A], stock: List[A]): List[A] =
          stream match {
            case Empty      => stock
            case Cons(h, t) =>
              f(h()) match {
                case true  => loop(t(), stock :+ h())
                case false => (loop(t(), stock))
              }
          }
        Stream(loop(c, List.empty[A]):_*)
    }

  def foldRight[B](init: B)(f: (A, => B) => B): B = {
    this match {
      case Empty      => init
      case Cons(h, t) =>
        f(h(), t().foldRight(init)(f))
    }
  }

  def exists(f: A => Boolean): Boolean =
    foldRight(false)((a, b) => f(a) || b)

  // Stream の要素のうち、指定された述語と全ての値がマッチしているかどうかチェっっくする forAll 関数を実装せよ
  def forAll(f: A => Boolean): Boolean =
    foldRight(true)((a, b) => {
      println("ForAll")
      f(a) && b
    })

  // foldRight を使って headOption を実装せよ
  def headOption2: Option[A] =
    foldRight(None: Option[A])((a, _) => Some(a))

  def map[B](f: A => B): Stream[B] =
    foldRight(Empty: Stream[B])((a, b) => {
      println("map")
      Cons[B](() => f(a), () => b)
    })

  def filter(f: A => Boolean): Stream[A] = {
    foldRight(Empty: Stream[A])((a, b) => {
      println("filter")
      if (f(a)) Cons(() => a, () => b)
      else      b
    })
  }

  def append[B >: A](v: => B): Stream[B] =
    foldRight(Cons(() => v, () => Empty))((a, b) =>
      Cons(() => a, () => b)
    )
}
case object Empty extends Stream[Nothing]
case class Cons[+A](h: () => A, t: () => Stream[A]) extends Stream[A]

object Stream {

  def cons[A](h: => A, t: => Stream[A]): Stream[A] = {
    lazy val head = h
    lazy val tail = t
    Cons(() => head, () => tail)
  }

  def empty[A]: Stream[A] = Empty

  def apply[A](as: A*): Stream[A] =
    if   (as.isEmpty) empty
    else cons(as.head, apply(as.tail: _*))
}
