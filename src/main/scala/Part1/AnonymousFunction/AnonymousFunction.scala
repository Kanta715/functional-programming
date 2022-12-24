package Part1.AnonymousFunction

import Part1.PolymorphicFunction.PolymorphicFunction

// 無名関数
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
