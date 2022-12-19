package Part1

// 副作用のない StringBuilder を作成
// Original StringBuilder
case class StringBuilder(string: String = "") {

  // Add string
  def append(str: String): StringBuilder =
    StringBuilder(string + str)
}
