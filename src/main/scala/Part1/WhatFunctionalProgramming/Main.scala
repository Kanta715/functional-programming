package Part1

import Part1.{ StringBuilder => OriginalStringBuilder }

object Main {

  def main(args: Array[String]): Unit = {

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
    val step5 = original.append("OriginalStringBuilder")
    val step6 = original.append(" ]")

    // 参照透過性があるオブジェクト
    println(step4) // StringBuilder([ )
    println(step5) // StringBuilder(OriginalStringBuilder)
    println(step6) // StringBuilder( ])
  }
}
