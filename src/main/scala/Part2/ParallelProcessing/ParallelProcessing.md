## 純粋関数型の並列処理
ここでは、並列処理と非同期処理を可能にする純粋関数型のライブラリを作成する。並列プログラミングは複雑だが、それらを純粋関数だけで表現することで、そうした複雑さを可能な限り単純化する。

ライブラリのユーザーが高いレベルでプログラムを記述できるようにすることで、プログラムが実行される方法にユーザーが無関心でいられるようにしたい。
以下の `parMap` のようなコレクション内の全ての要素に関数 f を簡単かつ同時に適用できるようにする。
```scala
val outputList = parMap(inputList)(f)
```

`Sequentail.sum` では、`foldLeft` を用いて畳み込みでコレクション内の数値を合計している。

`Parallerl.sum` では、`IndexedSeq.splitAt` を用いて分割した2つのコレクションを並列して再帰的に計算する。
### データ型と関数の選択
```scala
object Sequential {

  def sum(ints: Seq[Int]): Int =
    ints.foldLeft(0)((a, b) => a + b)
}

object Parallel {

  def sum(ints: IndexedSeq[Int]): Int =
    if (ints.size <= 1)
      ints.headOption getOrElse 0
    else {
      val (l, r) = ints.splitAt(ints.size / 2)
      sum(l) + sum(r)
    }
}
```

整数の合計計算は非常に高速なため、並列化によってオーバーヘッドが増えることになる。

ここでは関数型ライブラリ設計の本質を捉えることを目的としているため、単純な例を用いて理解を深めている。

関数型の設計では、特殊なケースを山ほど示すのではなく、単純で合成可能な、基本的なデータ型と関数を構築していくことにより、表現力を鍛えることを目指す。
```scala
val ints = 1 to 10000000

val start = System.currentTimeMillis()
val seq   = Sequential.sum(ints)
val end   = System.currentTimeMillis()
println("/-- 逐次処理 -----------------------------")
println(start - end + "ms")
println(seq)
println()

val start2 = System.currentTimeMillis()
val par    = Parallel.sum(ints)
val end2   = System.currentTimeMillis()
println("/-- 並列処理 -----------------------------")
println(start2 - end2 + "ms")
println(par)

// /-- 逐次処理 -----------------------------
// -96ms
// -2004260032
//
// /-- 並列処理 -----------------------------
// -282ms
// -2004260032
```
