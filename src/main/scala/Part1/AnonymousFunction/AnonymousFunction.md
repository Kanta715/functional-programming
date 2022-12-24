## 無名関数
無名関数とは、名前付きの関数ではない関数のことをいう。

高階関数では、無名関数または関数リテラルとして呼び出せると便利なことがよくある。
名前付きメソッドとして定義せずに、関数リテラルとすることで、インラインで定義できる。
```scala
object AnonymousFunction {

  def main(args: Array[String]): Unit = {

    val spaghetti: Array[String] = Array(
      "カルボナーラ",
      "ナポリタン",
      "ペペロンチーノ",
      "ボロネーゼ"
    )

    val aglioOlio: String = "アーリオオーリオ"

    // PolymorphicFunction.find で無名関数を使う
    val result:  Option[String] = PolymorphicFunction.find(spaghetti, (s: String) => s == "ペペロンチーノ")
    val result2: Option[String] = PolymorphicFunction.find(spaghetti, (s: String) => s == aglioOlio)

    println(result)  // Some(ペペロンチーノ)
    println(result2) // None
  }
}

```
