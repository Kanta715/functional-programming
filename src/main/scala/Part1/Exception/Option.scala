package Part1.Exception

sealed abstract class Option[+A] {

  def map[B](f: A => B): Option[B]
  def flatMap[B](f: A => Option[B]): Option[B]
  def getOrElse[B >: A](default: => B): B
  def orElse[B >: A](default: => B): Option[B]
  def filter(f: A => Boolean): Option[A]
  def isDefined: Boolean
  def isEmpty: Boolean
}
case class  Some[A](v: A) extends Option[A] {

  override def map[B](f: A => B): Option[B] = Some(f(v))

  override def flatMap[B](f: A => Option[B]): Option[B] = f(v)

  override def getOrElse[B >: A](default: => B): B = v

  override def orElse[B >: A](default: => B): Option[B] = Some(v)

  override def filter(f: A => Boolean): Option[A] = if (f(v)) Some(v) else None

  override def isDefined: Boolean = true

  override def isEmpty: Boolean = false
}

case object None extends Option[Nothing] {

  override def map[B](f: Nothing => B): Option[B] = None

  override def flatMap[B](f: Nothing => Option[B]): Option[B] = None

  override def getOrElse[B >: Nothing](default: => B): B = default

  override def orElse[B >: Nothing](default: => B): Option[B] = Some(default)

  override def filter(f: Nothing => Boolean): Option[Nothing] = None

  override def isDefined: Boolean = false

  override def isEmpty: Boolean = true
}

object Option {

  def mean(seq: Seq[Double]): Option[Double] =
    if (seq.isEmpty) None
    else             Some(seq.sum / seq.length)

  // 練習問題: flatMap をベースとして variance 関数を実装瀬せよ
  def variance(seq: Seq[Double]): Option[Double] = {
    val calc: Double => Double = (v: Double) => math.pow(v - seq.length, 2)
    val f: Seq[Double] => Option[Double] = (seq: Seq[Double]) => if (seq.isEmpty) None else Some(seq.map(calc(_)).sum)
    val seqOpt: Option[Seq[Double]] = Some(seq)
    seqOpt.flatMap(f)
  }

  def lift[A, B](f: A => B): Option[A] => Option[B] = _ map f

  // 2つの Option をとり、どちらかが None の場合は、戻り値が None になる関数
  def map2[A, B, C](opt: Option[A], opt2: Option[B])(f: (A, B) => C): Option[C] =
    (opt, opt2) match {
      case (Some(v), Some(v2)) => Some(f(v, v2))
      case _                   => None
    }

  // Option のリストを 1つの Option にまとめる sequence 関数を実装せよ
  // 1つでも None がある場合は、戻り値は None になる
  def sequence[A](optList: List[Option[A]]): Option[List[A]] = {
    val flagAndList = optList.foldLeft((true, List.empty[A])) { case ((flag, list), opt) =>
      val (isDefined, seq) = opt match {
        case Some(v) => (true, list :+ v)
        case None    => (false, list)
      }
      ((isDefined && flag), seq)
    }
    flagAndList._1 match {
      case true  => Some(flagAndList._2)
      case false => None
    }
  }

  def traverse[A, B](list: List[A])(f: A => B): Option[List[B]] =
    if (list.isEmpty) None else Some(list.map(f(_)))
}
