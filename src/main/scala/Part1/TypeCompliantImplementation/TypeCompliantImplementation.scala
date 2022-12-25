package Part1.TypeCompliantImplementation

object TypeCompliantImplementation {

  def main(args: Array[String]): Unit = {

    val user: User = User("Shohei Ohtani", 28)

    val print: (User, String) => Unit = (user: User, otherInfo: String) => {
      val userInfo: String =
        s"""
          |😊ユーザー情報😊
          |
          |名前: ${user.name}
          |年齢: ${user.age}
          |
          |その他の情報: $otherInfo
          |""".stripMargin
      println(userInfo)
    }

    // 部分関数を使用
    val andThen: String => Unit = partial[User, String, Unit](user, print)

    println(andThen)

    val info: String =
      """
        |身長: 193cm
        |体重: 95kg
        |
        |アピールポイント:
        |・Max 165km のストレート
        |・長打力
        |""".stripMargin

    // 残りの引数を渡す
    andThen(info)

    // --------------------------------------------------------
    // 練習問題
    println("")
    println("練習問題")
    println("")
    val p: (String, Int) => String = (x, y) => x + y.toString
    println(curry(p))

    val p2: String => String => String = (s: String) => (s2: String) => s + s2
    println(uncurry(p2))

    val p3A: Int => String = x => x.toString
    val p3B: String => Unit = x => println(x)
    println(compose(p3B, p3A))
  }

  // 型シグネチャの指定により、1つの実装に絞られる
  def partial[A, B, C](a: A, f: (A, B) => C): B => C =
    (b: B) => f(a, b)

  // ------------------------------------------------
  // 練習問題
  // 1. 以下を実装せよ
  // def curry[A, B, C](f: A => B => C): A => (B => C)
  def curry[A, B, C](f: (A, B) => C): A => (B => C) =
    (a: A) => (b: B) => f(a, b)

  // 2. 1 で実装した curry の逆になる関数 uncurry を実装せよ
  def uncurry[A, B, C](f: A => B => C): (A, B) => C =
    (a: A, b: B) => f(a)(b)

  // 3. 2つの関数を合成する高階関数を実装せよ
  def compose[A, B, C](f: B => C, g: A => B): A => C =
    (a: A) => f(g(a))
}
