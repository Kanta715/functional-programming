package Part1.Exception

sealed abstract class Option[+A]
case class  Some[A](v: A) extends Option[A]
case object None          extends Option[Nothing]

object Option {

  def mean(seq: Seq[Double]): Option[Double] =
    if (seq.isEmpty) None
    else             Some(seq.sum / seq.length)
}
