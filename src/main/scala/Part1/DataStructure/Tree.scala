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

  def depth[A](tree: Tree[A]): Int = {
    def loop(tree: Tree[A], num: Int): Int = {
      tree match {
        case Leaf(_)      => num + 1
        case Branch(l, r) => max(loop(l, num + 1), loop(r, num + 1))
      }
    }
    loop(tree, 0)
  }

  private def max(num: Int, num2: Int): Int =
    if (num > num2) num else num2
}
