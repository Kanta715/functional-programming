package Part1.DataStructure

object DataStructure {

  def main(args: Array[String]): Unit = {

    val nil:    List[Double]  = Nil
    val vList:  VList[String] = VList("関数型", Nil)
    val vList2: VList[Int]    = VList(1, VList(2, VList(10, VList(368, Nil))))

    val value: List[String] = List.map(vList, (v: String) => v + " --------------- オブジェクト指向")
    println(value)

    val value2: List[Age] = List.map(vList2, (n: Int) => Age(n))
    println(value2)

    val value3: List[String] = List.filter(vList, (s: String) => s == "関数型")
    println(value3)

    val value4: List[Int] = List.filter(vList2, (v: Int) => v != 1)
    println(value4)

    val value5: List[Age] = List.dropHead(value2)
    println(value5)

    val value6: List[Age] = List.setHead(value5, (v: Age) => v.copy(v.v + 2000000000))
    println(value6)

    val vList3: VList[Int] = VList(1, VList(2, VList(10, VList(368, VList(2000, VList(1, VList(11, VList(80, VList(2304, Nil)))))))))
    val value7: List[Int]  = List.drop(vList3, 8)
    println(value7)

    val value8: List[Int] = List.dropWhile(vList3, (v: Int) => v == 1000000000)
    println(value8)
    val value9: List[Int] = List.dropWhile(vList3, (v: Int) => v == 80)
    println(value9)

    val value10: Seq[Age] = List.foldRight(vList3, Seq.empty[Age])((x, y) => Age(x) +: y)
    println(value10)
  }

  case class Age(v: Int)
}
