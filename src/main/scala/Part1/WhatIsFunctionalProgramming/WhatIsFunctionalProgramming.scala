package Part1.WhatIsFunctionalProgramming

import java.lang.{ StringBuilder => JavaStringBuilder }
import Part1.WhatIsFunctionalProgramming.{ StringBuilder => OriginalStringBuilder }

object WhatIsFunctionalProgramming {

  def main(args: Array[String]): Unit = {

    // 副作用のあるオブジェクト
    // Java StringBuilder
    val builder = new JavaStringBuilder

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
  }

}
