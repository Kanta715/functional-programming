package Part1.Exception

sealed abstract class Option[+A] {

  def map[B](f: A => B): Option[B]
  def flatMap[B](f: A => Option[B]): Option[B]
  def getOrElse[B >: A](default: => B): B
  def orElse[B >: A](default: => B): Option[B]
  def filter(f: A => Boolean): Option[A]
}
case class  Some[A](v: A) extends Option[A] {

  override def map[B](f: A => B): Option[B] = Some(f(v))

  override def flatMap[B](f: A => Option[B]): Option[B] = f(v)

  override def getOrElse[B >: A](default: => B): B = v

  override def orElse[B >: A](default: => B): Option[B] = Some(v)

  override def filter(f: A => Boolean): Option[A] = if (f(v)) Some(v) else None
}

case object None extends Option[Nothing] {

  override def map[B](f: Nothing => B): Option[B] = None

  override def flatMap[B](f: Nothing => Option[B]): Option[B] = None

  override def getOrElse[B >: Nothing](default: => B): B = default

  override def orElse[B >: Nothing](default: => B): Option[B] = Some(default)

  override def filter(f: Nothing => Boolean): Option[Nothing] = None
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
}
