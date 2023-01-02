package Part1.DataStructure

import scala.util.Random

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

    // foldRight
    val value10: Seq[Age] = List.foldRight(vList3, Seq.empty[Age])((x, y) => Age(x) +: y)
    println(value10)

    val value11: Int = List.length(vList2)
    println(value11)

    // foldLeft
    val value12: Seq[Age] = List.foldLeft(vList2, Seq.empty[Age])((x, y) => y :+ Age(x))
    println(value12)

    val value13: List[Int] = List.reverse(vList2)
    println(value13)

    val value14: String = List.foldRightOfBaseLeft(vList2, "")(_.toString + _) // 1210368
    val value15: String = List.foldLeft(vList2, "")(_.toString + _)            // 3681021
    println(value14)
    println(value15)

    val value16: List[Int] = List.append(vList3, 1234567890)
    println(value16)

    val l:  List[String] = VList("あ", VList("い", VList("う", VList("え", VList("お", Nil)))))
    val l2: List[String] = VList("か", VList("き", VList("く", VList("け", VList("こ", Nil)))))
    val value17: List[String] = List.connect(l, l2)
    println(value17)

    val numList: List[Int] = VList(2, VList(3, VList(9, Nil)))
    val value18: List[Int] = List.addOne(numList)
    println(value18)

    val value19: List[Int] = List.flatMap(numList, (x: Int) => List.apply(x * 10))
    println(value19)

    val leaf:  Leaf[String] = Leaf("Leaf")
    val leaf2: Leaf[String] = Leaf("Leaf2")
    val leaf3: Leaf[String] = Leaf("Leaf3")
    val leaf4: Leaf[String] = Leaf("Leaf4")

    val branch:  Branch[String] = Branch(leaf,  leaf2)
    val branch2: Branch[String] = Branch(leaf3, leaf4)

    val root: Tree[String] = Branch(branch, branch2)

    val value20: Int = Tree.size(root)
    println(value20)

    val intLeaf:  Leaf[Int] = Leaf(1)
    val intLeaf2: Leaf[Int] = Leaf(2)
    val intLeaf3: Leaf[Int] = Leaf(100)
    val intLeaf4: Leaf[Int] = Leaf(20)

    val intBranch: Branch[Int]  = Branch(intLeaf, intLeaf2)
    val intBranch2: Branch[Int] = Branch(intLeaf3, intBranch)

    val root2: Tree[Int] = Branch(intLeaf4, intBranch2)

    val value21: Int = Tree.maximum(root2)
    println(value21)

    val value22: Map[Int, Seq[Tree[Int]]] = Tree.depth(root2)
    println(value22)

    val value23: Tree[String] = Tree.map(root2, (x: Int) => "Value " + x)
    println(value23)

    def func(tree: Tree[Int]): String = tree match {
      case Leaf(v)      => v.toString + "__" + Random.nextInt(100) + "  "
      case Branch(l, r) => func(l) + func(r)
    }
    val value24: String = Tree.fold(root2, func(_))
    println(value24)
  }

  case class Age(v: Int)
}
