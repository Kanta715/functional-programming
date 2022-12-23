## 1. 関数型プログラミングとは
関数型プログラミングでは、純粋関数だけを使用してプログラムを構築することを前提としている。

### 1-2. 純粋関数とは
純粋関数とは、副作用のない関数のことを指す。
- 変数を変更する
- データ構造を直接変更する
- オブジェクトのフィールドを設定する
- 例外をスローする、またはエラーで停止する
- コンスールに出力する、またはユーザー入力を読み取る
- ファイルを読み取る、またはファイルに書き込む
- 画面上に描画する

関数型プログラミングはプログラムの書き方を制約するものである。ただし、表現可能なプログラムの「種類」を制約するものではない。

純粋関数は式が参照透過であるなど、書籍では難しいことを長々書いている。自分の理解で言うと、参照透過性は「関数の式 = 関数の結果」でければならないということだと思う。

Java の StringBuilder には副作用があり、参照透過性が破壊されている。append したあとに代入している変数の値をみると、全ての変数が同じ値を持っている。Java の StringBuilder はシングルトンであるためミュータブルであり、append の前後の処理を意識しなければならない。

今回作成したオリジナルの StringBuilder は、参照透過性（関数の式 = 関数の結果）を意識して実装した。 append 後に代入した変数を出力すると、append で返した値を処理の順序に従って出力している。

```Scala
// 副作用のない StringBuilder を作成
// Original StringBuilder
case class StringBuilder(string: String = "") {
  // Add string
  def append(str: String): StringBuilder =
    StringBuilder(string + str)

  override def toString: String = string
}

// 副作用のあるオブジェクト
// Java StringBuilder
val builder = new java.lang.StringBuilder

val step1 = builder.append("[ ")
val step2 = builder.append("JavaStringBuilder")
val step3 = builder.append(" ]")

// 参照透過性がないもの
println(step1) // [ JavaStringBuilder ]
println(step2) // [ JavaStringBuilder ]
println(step3) // [ JavaStringBuilder ]

// Original StringBuilder
val original = OriginalStringBuilder()

val step4 = original.append("[ ")
val step5 = step4.append("OriginalStringBuilder")
val step6 = step5.append(" ]")

// 参照透過性があるオブジェクト
println(step4.toString) // [
println(step5.toString) // [ OriginalStringBuilder
println(step6.toString) // [ OriginalStringBuilder ]
```

## 2. 多相関数：型の抽象化
ここまで定義してきたのは関数は、**単相関数（monomorphic function）** である。単相関数とは、1つの型のデータだけを操作する関数のことをいう。

```scala
object PolymorphicFunction {

  def main(args: Array[String]): Unit = {

    val strArray: Array[String] = Array(
      "Functional Programming",
      "Object Oriented Programming",
      "Procedural Programming"
    )

    val str:  String = "Functional Programming"
    val str2: String = "関数型プログラミング"

    println(findForStringType(strArray, str))  // Some(Functional Programming)
    println(findForStringType(strArray, str2)) // None

    // ----------------------------------------------------

    val intArray: Array[Int] = Array(
      1, 2, 3, 4, 5
    )

    val intCheckForTwo: Int => Boolean = (int: Int) => int == 2

    // ----------------------------------------------------

    case class T(v: String)
    val tArray: Array[T] = Array(
      T("A"),
      T("B"),
      T("C"),
      T("D"),
      T("E")
    )

    val tCheckForD: T => Boolean = (t: T) => t equals T("D")
    val tCheckForF: T => Boolean = (t: T) => t equals T("F")

    // ----------------------------------------------------

    println(find(intArray, intCheckForTwo)) // Some(2)

    println(find(tArray, tCheckForD)) // Some(T(D))
    println(find(tArray, tCheckForF)) // None

  }

  /**
   * 単相関数
   * str で指定した文字列と等価である最初の値を Some(_) で返す
   * 等価の値が見つからない場合は、None を返す
   */
  private def findForStringType(array: Array[String], str: String): Option[String] = {
    def loop(n: Int): Option[String] = {
      if (array.length <= n) None
      else if (array(n) == str) Some(array(n))
      else loop(n.+(1))
    }
    loop(0)
  }

  /**
   * 多相関数
   * A 型として抽象的に定義することで、汎用的に使える関数になる
   */
  private def find[A](array: Array[A], f: A => Boolean): Option[A] = {
    def loop(n: Int): Option[A] = {
      if (array.length <= n) None
      else if (f(array(n))) Some(array(n))
      else loop(n.+(1))
    }
    loop(0)
  }
}
```
