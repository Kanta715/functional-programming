package Part1.TypeCompliantImplementation

object TypeCompliantImplementation {

  def main(args: Array[String]): Unit = {

    val user: User = User("Shohei Ohtani", 28)

    val print: (User, String) => Unit = (user: User, otherInfo: String) => {
      val userInfo: String =
        s"""
          |ðã¦ã¼ã¶ã¼æå ±ð
          |
          |åå: ${user.name}
          |å¹´é½¢: ${user.age}
          |
          |ãã®ä»ã®æå ±: $otherInfo
          |""".stripMargin
      println(userInfo)
    }

    // é¨åé¢æ°ãä½¿ç¨
    val andThen: String => Unit = partial[User, String, Unit](user, print)

    println(andThen)

    val info: String =
      """
        |èº«é·: 193cm
        |ä½é: 95kg
        |
        |ã¢ãã¼ã«ãã¤ã³ã:
        |ã»Max 165km ã®ã¹ãã¬ã¼ã
        |ã»é·æå
        |""".stripMargin

    // æ®ãã®å¼æ°ãæ¸¡ã
    andThen(info)

    // --------------------------------------------------------
    // ç·´ç¿åé¡
    println("")
    println("ç·´ç¿åé¡")
    println("")
    val p: (String, Int) => String = (x, y) => x + y.toString
    println(curry(p))

    val p2: String => String => String = (s: String) => (s2: String) => s + s2
    println(uncurry(p2))

    val p3A: Int => String = x => x.toString
    val p3B: String => Unit = x => println(x)
    println(compose(p3B, p3A))
  }

  // åã·ã°ããã£ã®æå®ã«ããã1ã¤ã®å®è£ã«çµããã
  def partial[A, B, C](a: A, f: (A, B) => C): B => C =
    (b: B) => f(a, b)

  // ------------------------------------------------
  // ç·´ç¿åé¡
  // 1. ä»¥ä¸ãå®è£ãã
  // def curry[A, B, C](f: A => B => C): A => (B => C)
  def curry[A, B, C](f: (A, B) => C): A => (B => C) =
    (a: A) => (b: B) => f(a, b)

  // 2. 1 ã§å®è£ãã curry ã®éã«ãªãé¢æ° uncurry ãå®è£ãã
  def uncurry[A, B, C](f: A => B => C): (A, B) => C =
    (a: A, b: B) => f(a)(b)

  // 3. 2ã¤ã®é¢æ°ãåæããé«éé¢æ°ãå®è£ãã
  def compose[A, B, C](f: B => C, g: A => B): A => C =
    (a: A) => f(g(a))
}
