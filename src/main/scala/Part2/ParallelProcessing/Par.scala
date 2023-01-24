package Part2.ParallelProcessing

case class Par[A](v: A)

object Par {

  def unit[A](a: => A): Par[A] = Par(a)

  def get[A](par: Par[A]): A = par.v

  def sum(ints: IndexedSeq[Int]): Int =
    if (ints.size <= 1)
      ints.headOption getOrElse 0
    else {
      val (l, r) = ints.splitAt(ints.size / 2)
      val sumL: Par[Int] = Par.unit(sum(l))
      val sumR: Par[Int] = Par.unit(sum(r))
      Par.get(sumL) + Par.get(sumR)
    }
}
