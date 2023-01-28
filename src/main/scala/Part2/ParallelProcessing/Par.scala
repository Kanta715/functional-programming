package Part2.ParallelProcessing

case class Par[A](v: A)

object Par {

  def unit[A](a: => A): Par[A] = Par(a)

  def get[A](par: Par[A]): A = par.v

  var str: String = "0"
  def sum(ints: IndexedSeq[Int]): Int = {
    if (ints.size <= 1) {
      val int = ints.headOption getOrElse 0
      str = str + int
      int
    } else {
      val (l, r) = ints.splitAt(ints.size / 2)
      val sumL: Par[Int] = Par.unit(sum(l))
      val sumR: Par[Int] = Par.unit(sum(r))
      Par.get(sumL) + Par.get(sumR)
    }
  }

  var str2: String = "0"
  def sum2(ints: IndexedSeq[Int]): Int = {
    if (ints.size <= 1) {
      val int = ints.headOption getOrElse 0
      str2 = str2 + int
      int
    } else {
      val (l, r) = ints.splitAt(ints.size / 2)
      Par.get(Par.unit(sum2(l))) + Par.get(Par.unit(sum2(r)))
    }
  }

  var str3: String = "0"
  def par(ints: IndexedSeq[Int]): Unit = {
    import scala.concurrent.{ Future, Await }
    import scala.concurrent.ExecutionContext.Implicits.global
    val v = Future(ints.foreach(int => Future{
      str3 = str3 + int
    }))
    Await.result(v, scala.concurrent.duration.Duration.Inf)
  }
}
