## 正格と遅延
これまでは、単方向リストの純粋関数型のデータ構造について map, filter, foldLeft などリストの演算を実装しながら学んだ。
こうした演算はそれぞれが入力をスキャンし、出力用の新しいリストを生成することをがわかった。

例として、ひと組のトランプから奇数のカードを抜き取り、クイーンを全て返すように言われたとする。
理想は、全てのカードを通しで調べるときに、クイーンと奇数のカードを同時に探したい。奇数のカードを抜き取った後、残りのカードからクイーンを探すよりも、作業を1回で済ませることができ効率的である。

以下のコードが行っているのは、前者の方である。
```scala
// map, filter などの純粋関数を都度実行すると、先頭から全ての値を走査し、新しい List を返す
// 以下の例だと単純な計算だが、3度も走査し、List を生成している
val list: List[Int] = List(1, 2, 3, 4, 5, 6).map(_ + 10).filter(_ % 2 == 0).map(_ * 3)
println(list)  // List(36, 42, 48)
/*
  細かく分けると以下のように中間のリストを生成している
  List(1, 2, 3, 4, 5, 6).map(_ + 10).filter(_ % 2 == 0).map(_ * 3)
  List(11, 12, 13, 14, 15, 16).filter(_ % 2 == 0).map(_ * 3)
  List(12, 14, 16).map(_ * 3)
  List(36, 42, 48)
 */
```
なんとかして、一連の変換を1回の処理にまとめたい。
`map` や `filter` といったループの自動的な融合は、**非正格性** を使って実現できるらしい。（**非正格性 == 遅延性**）

ここでは、**非正格性** がどのようなものか学習し、複数の変換を融合する遅延リスト型を実装する。 

### 正格関数と非正格関数
遅延リストの例をみる前に、基礎の理解する。

#### 正格と非正格とは
非正格性は関数の特性である。非正格関数では、その引数の1つ以上を評価しないという選択が可能。

逆に、**正格関数**では、引数が常に評価される。正格関数はほとんどのプログラミング言語の標準になっている。
ほとんどの言語がサポートしているのは引数が完全に評価されることを期待する関数だけである。
特に明記しない限り、Scala の関数定義は全て正格であると考えて良い。ここまで定義してきた関数は全て正格である。

例
```scala
def square(x: Double): Double = x * x
```
`square` 関数は正格であるため、`square(41.0 + 1.0)` の呼び出しでは、計算済みの `42.0` という値が渡されることになる。
`square(sys.error("Error!!"))` の呼び出しでは、`square` の本体に入る前に `sys.error()` 式が評価されるため、`square` が何かをする前に例外が発生する。

非正確性の概念には馴染みがある。例えば、短絡理論関数 `&&` および `||` が含まれるが、これらは非正格である。
`&&`, `||` は引数を評価しないことを選択できる関数として考えることができる。`&&` 関数は `Boolean` 型の引数を2つ受け取るが、2つ目の引数を評価するのは、1つ目の引数が `true` の場合のみ。
```scala
false && {
  println("評価")
  true
} // 何も出力しない

true || {
  println("評価")
  false
} // 何も出力しない
```

`if` 制御構文も非正格の一例である。

`if` は Scala の言語に組み込まれている構造だが、`Boolean` 型の条件、条件が `true` の場合に返す `A` 型の式、および条件が `false` の場合に返す、`A` 型の式という3つのパラメータを受け取る関数として考えることができる。
関数としての `if` は、引数を全て評価しないため、非正格である。

もっと正確に言うと、実行する分岐を判定するために条件を常に評価するため、条件パラメータについては正格である。分岐については、条件に基づいて評価するため、非正格ということになる。
```scala
if (list.isEmpty) sys.error("評価されました！！！") else list // エラーは評価されていない
```

Scala で非正格関数を記述するには、引数の一部を評価されない状態で受け取る。非正格関数である `if` は以下のようになる。
```scala
def if2[A](cond: Boolean, onTrue: () => A, onFalse: () => A): A =
  if (cond) onTrue() else onFalse()

if2(true, () => println("true!!!"), () => sys.error("false!!!")) // true!!!
```
評価せずに渡したい引数の手前に、`() =>` が付いている。
`() => A`型の値は、0個の引数を取り、`A` を返す関数。評価されない形式の式を一般に **サンク(thunk)** と呼ぶ。
サンクに対しては、式の評価と結果の取得を**強制**することができる。そのためには、`onTrue()` などのように、関数を呼び出してからの引数リストを渡す。
同様に、`if2` の呼び出し元が、明示的に作成する必要もある。そのための構文は、関数リテラル構文の規約に従う。

各非正格パラメータの代わりに引数なしの関数を渡した後、この関数を本体から明示的に呼び出して結果を取得している。これは、よくあるケースのため、より便利な構文が用意されている。
```scala
def if3[A](cond: Boolean, onTrue: => A, onFalse: => A): A =
  if (cond) onTrue else onFalse

if3(true, println("TRUE!!!"), sys.error("FALSE!!!")) // TRUE!!!
```
評価せずに渡したい引数の型の手前に `=>` がある、。この `=>` の付いた引数を評価するために関数本体で特別なことはしなくて良い。識別子を通常通り参照するだけ。

呼び出し元も特別なことはしなくて良い。通常の関数構文を使用できる。

評価されずに関数に渡される引数は、関数の本体内で参照されている場所ごとに1だけ評価される。Scala は引数の評価結果を（デフォルトでは）キャッシュしない。

結果を1回だけ評価したい場合は、val 宣言時に `lazy` キーワードを追加する。`lazy val` 宣言の右辺の評価が最初に参照される時まで先送りされる。
また、その後の参照で評価を繰り返し行うことがないよう、結果がキャッシュされる。Scala の非正格関数は引数を**値渡し**ではなく**名前渡し**で受け取る。

[値渡しと名前渡しの違いをコードで見る](https://www.sassy-blog.com/entry/20171221/1513823305)
```scala
def notCache(b: Boolean, i: => Int): Int = if (b) i + i else 0

notCache(true, {
  println("Not cache !!!!!")
  100
})
// Not cache !!!!!
// Not cache !!!!!

def cache(b: Boolean, i: => Int): Int = {
  lazy val j = i
  if (b) j + j else 0
}

cache(true, {
  println("Cache !!!!!")
  100
})
// Cache !!!!!
```

### 遅延リスト
ここでは、遅延リスト（ストリーム）を使用する関数型プログラムの効率性とモジュール性を向上させるために、遅延性をどのように利用できるかについて見ていく。（ [モジュール性のメリット](https://learn.liferay.com/dxp/latest/ja/liferay-internals/architecture/the-benefits-of-modularity.html#id1) ）
```scala
trait Stream[+A] {
  def headOption: Option[A] =
    this match {
      case Empty      => None
      case Cons(h, _) => Some(h())
    }
}
case object Empty extends Stream[Nothing]
case class Cons[+A](h: () => A, t: () => Stream[A]) extends Stream[A]

object Stream {

  def cons[A](h: => A, t: => Stream[A]): Stream[A] = {
    lazy val head = h
    lazy val tail = t
    Cons(() => head, () => tail)
  }

  def empty[A]: Stream[A] = Empty

  def apply[A](as: A*): Stream[A] =
    if   (as.isEmpty) empty
    else cons(as.head, apply(as.tail: _*))
}
```

#### ストリームを記憶し、再計算を回避する
一般的には、Cons ノードの値が強制的に取得された時点で、それをキャッシュしておきたい。以下の場合は、`double` が2回実行される。
```scala
val double: Int => Int = (x: Int) => {
  println("execute")
  x * 2
}
val cons = Cons(() => double(1), () => Part1.StrictAndDelay.Stream(10, 100, 1000))
// 2回 double が呼ばれている
val head  = cons.headOption
val head2 = cons.headOption
println(head)
println(head2)
// execute
// execute
// Some(2)
// Some(2)
```
一般的には、スマートコンストラクタを定義することで、この問題を回避する。

スマートコンストラクタとは、追加の不変条件を満たすデータ型、あるいはパターンマッチングに使用される「本物」のコンストラクタとはシグネチャが少しことなるデータ型を生成する関数のこと。

ここでは、上記で定義した`cons` がスマートコンストラクタになる。

`cons` スマートコンストラクタは、Cons の head, tail に対する名前渡しの引数を記憶する。サンクの処理が実行されるのは、サンクの強制的な評価が最初に行われた時だけになる。それ以降は、キャッシュされた遅延値（lazy val）が返される。

```scala
val s      = Part1.StrictAndDelay.Stream.cons(double(1), stream)
val sHead  = s.headOption
val sHead2 = s.headOption

// double が1度しか実行されない
println(sHead)
println(sHead2)
// execute
// Some(2)
// Some(2) <- キャッシュされた値
```

### プログラムの記述と評価の切り分け
関数型プログラミングのテーマの1つは**関心の分離**である。 処理の記述をその実際の実行から切り離す必要がある。

Stream では、一連の要素を生成するための処理を組み立てることが可能であり、そうした処理のステップはそれらの要素が必要になるまで実行されない。（名前渡しだから）
それにより、一部だけを評価することが可能になる。

以下の例では、Stream の exists 関数は遅延評価により、一部の値に対して関数が実行されている
```scala
def foldRight[B](init: B)(f: (A, => B) => B): B = {
  this match {
    case Empty      => init
    case Cons(h, t) =>
      f(h(), t().foldRight(init)(f))
  }
}

def exists(f: A => Boolean): Boolean =
  foldRight(false)((a, b) => f(a) || b)

val result = stream.exists(v => {
  println("exists")
  v == 100
})
println(result)
// exists
// exists
// true

import Part1.DataStructure.List
val f: Int => Boolean = (x: Int) => {
  println("Execute")
  x == 100
}
val result2 = List.exists(List.apply(10, 100, 1000), f)
println(result2)
// Execute
// Execute
// Execute
// true
```

Stream で実装した map, filter などは答えを完全に生成しない。結果として得られた Stream の要素を他の処理が調べるまで、その Stream を実際に生成する処理は実行されない。

この性質のおかげで、中間結果を完全にインスタンス化することなく関数を呼び出せる。

Stream の map, filter は交互に実行されている。
```scala
// Stream
val result9 = cons2.map(_ + 100).filter(_ != 200).toList
println(result9)
// map
// filter
// map
// filter
// map
// filter
// map
// filter
// map
// filter
// List(102, 120, 300, 2100, 20100)

// List
println()
val listResult = List(1, 10, 100, 1000, 10000).mapS(_ + 100).filterS(_ != 100)
println(listResult)
// MapS
// MapS
// MapS
// MapS
// MapS
// MapS
// FilterS
// FilterS
// FilterS
// FilterS
// FilterS
// FilterS
// VList(101,VList(110,VList(200,VList(1100,VList(10100,Nil)))))
```

### 無限ストリームと余再帰
ここまでの関数は漸進的であるため、**無限ストリーム**にも対応する。
```scala
scala> import Part1.StrictAndDelay.Stream
import Part1.StrictAndDelay.Stream

scala> val ones: Stream[Int] = Stream.cons(1, ones)
val ones: Part1.StrictAndDelay.Stream[Int] = Cons(Part1.StrictAndDelay.Stream$$$Lambda$4172/1283856354@79370164,Part1.StrictAndDelay.Stream$$$Lambda$4173/1057769576@2af9b564)

scala> ones.take(5).toList
val res0: List[Int] = List(1, 1, 1, 1, 1)

scala> ones.map(_ + 1).exists(_ % 2 == 0)
map
val res1: Boolean = true
```

上記のケースではすぐに結果が返される。しかし、少し間違えるといつもでも終了しないスタックセーフではない式を書いてしまいやすい。
明確な答えを持って終了するための要素が永遠に検出されないため、要素をさらに調べる作業が永遠に続く。
```scala
scala> ones.forAll(_ == 1)
 ⋮
java.lang.StackOverflowError
  at sun.nio.cs.UTF_8$Encoder.encodeLoop(UTF_8.java:691)
  at java.nio.charset.CharsetEncoder.encode(CharsetEncoder.java:579)
  at sun.nio.cs.StreamEncoder.implWrite(StreamEncoder.java:271)
  at sun.nio.cs.StreamEncoder.write(StreamEncoder.java:125)
```

余再帰がわからん
