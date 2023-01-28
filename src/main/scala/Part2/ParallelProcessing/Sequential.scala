package Part2.ParallelProcessing

object Sequential {

  def sum(ints: Seq[Int]): Int =
    ints.foldLeft(0)((a, b) => a + b)
}
