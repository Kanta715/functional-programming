package Part1.Exception

object NotOption {

  def mean(seq: Seq[Double]): Double =
    if  (seq.isEmpty) throw new ArithmeticException("空のリストです")
    else seq.sum / seq.length

  def mean2(seq: Seq[Double], onEmpty: Double): Double =
    if  (seq.isEmpty) onEmpty
    else seq.sum / seq.length
}
