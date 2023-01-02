package Part1.DataStructure

sealed trait Tree[+A]
case class Leaf[A](value: A) extends Tree[A]
case class Branch[A](left: Tree[A], right: Tree[A]) extends Tree[A]

object Tree {

  def size[A](tree: Tree[A]): Int = {
    def loop(tree: Tree[A], num: Int): Int = {
      tree match {
        case Leaf(_)      => num + 1
        case Branch(l, r) => loop(l, num) + loop(r, num) + 1
      }
    }
    loop(tree, 0)
  }

  def maximum(tree: Tree[Int]): Int = {
    def loop(tree: Tree[Int], num: Int): Int = {
      tree match {
        case Leaf(v)      => max(num, v)
        case Branch(l, r) => max(loop(l, num), loop(r, num))
      }
    }
    loop(tree, 0)
  }

  def depth[A](tree: Tree[A]): Map[Int, Seq[Tree[A]]] = {
    var map: Map[Int, Seq[Tree[A]]] = Map.empty[Int, Seq[Tree[A]]]
    def loop(tree: Tree[A], num: Int, path: Seq[Tree[A]]): Unit  = {
      tree match {
        case v@Leaf(_)      => map = map ++ Map[Int, Seq[Tree[A]]](num + 1 -> (path :+ v))
        case v@Branch(l, r) =>
          loop(l, num + 1, path :+ v)
          loop(r, num + 1, path :+ v)
      }
    }
    loop(tree, 0, Seq.empty)
    val keys     = map.keys.toSeq
    val depthKey = keys.max
    Map(depthKey -> map.get(depthKey).getOrElse(Seq.empty))
  }

  def map[A, B](tree: Tree[A], f: A => B): Tree[B] = {
    tree match {
      case Leaf(v: A)   => Leaf(f(v))
      case Branch(l, r) => Branch(map(l, f), map(r, f))
    }
  }

  def fold[A, B](tree: Tree[A], f: Tree[A] => B): B =
    f(tree)

  private def max(num: Int, num2: Int): Int =
    if (num > num2) num else num2
}
