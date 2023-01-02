## 関数型プログラミングのデータ構造
関数型データ構造は、純粋関数のみを使って操作される。純粋関数では、データを直接変更したり、他の副作用を発生させてはならない。
したがって、関数型データ構造はイミュータブルである。

例えば、空のリストは、整数値の 3 や 4 と同じように永続的でイミュータブルである。また、3 + 4 を評価した結果として、3 や 4 はそのままで 7 という新しい数字が得られるのと同様に、2つのリストを連結すると新しいリストが作成され 2つの入力は変化しない。

Nil, VList はデータコンストラクタと呼ばれる。
型パラメータを多相とすることで、`String` `Int` などあらゆる型からなるリストを定義できる。

Nil:   空のリスト

VList: 空ではないリスト
```scala
sealed trait List[+A]

final case object Nil extends List[Nothing]

final case class VList[A](head: A, tail: List[A]) extends List[A]

object List {

  def apply[A](list: A*): List[A] =
    if (list.isEmpty) Nil else VList(list.head: A, apply[A](list.tail: _*))
}
```

## リストの再帰と高階関数の一般化
重複している部分を関数の引数として抽出すると、それらを一般化できる
```scala
  def sum(ints: List[Int]): Int =
    ints match {
      case Nil         => 0
      case VList(h, t) => h + sum(t)
    }

  def product(ds: List[Double]): Double =
    ds match {
      case Nil         => 1.0
      case VList(h, t) => h * product(t)
    }

  /**
   * 上記のような sum, product があった場合、重複している部分を関数の引数として抽出すると、それらを一般化できる
   */
  def foldRight[A, B](list: List[A], init: B)(f: (A, B) => B): B = {
    list match {
      case Nil                           => init
      case VList(head: A, tail: List[A]) => f(head, foldRight(tail, init)(f))
    }
  }

  def sum2(ints: List[Int]): Int =
    foldRight(ints, 0)(_ + _)

  def product2(ds: List[Double]): Double =
    foldRight(ds, 1.0)(_ * _)
```