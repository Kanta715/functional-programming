## 例外
例外によって参照透過性が失われてしまうのはなぜか。また、それがなぜ問題なのか。

y が参照透過ではない。参照透過な式は参照先の式と置き換えるとが可能で、この置き換えによってプログラムの意味が変化しないはずである。

`x + y` の `y` を `throw new Exception("")` に置き換えた場合 `try` ブロックの中で例外が発生するようになるため、結果が異なる。
```scala
object Faile {

  val SYSTEM_ERROR_CODE = 500

  // 例外を投げる
  def failing(int: Int): Int = {
    val y: Int = throw new Exception("関数の実行に失敗しました")
    try {
      val x: Int = 50 + 50
      x + y
    } catch {
      case e: Exception => SYSTEM_ERROR_CODE
    }
  }
  // java.lang.Exception: 関数の実行に失敗しました
  //    at Part1.Exception.Faile$.failing(Faile.scala:9)
  //    :

  // try 内で例外が投げられ、catch することで、エラーコードを返す
  def failing2(int: Int): Int = {
    try {
      val x: Int = 50 + 50
      x + ((throw new Exception("関数の実行に失敗しました")): Int)
    } catch {
      case e: Exception => SYSTEM_ERROR_CODE
    }
  }
  // 500
  // y を throw new Exception に置き換えた場合、
  // try ブロックで例外が発生し catch されるため、failingFn と結果が異なる
}
```
参照透過な式の意味は**コンテキストに依存せず**、ローカルでの推論が可能であるのに対し、参照透過でない式の意味は**コンテキストに依存**し、よりグローバルな推論が必要となる。

上記の式では、参照透過な式 `50 + 50` の意味は、この式が埋め込まれている式に依存しない。この式は永遠に `100` と等しくなる。

対して、`throw new Exception` の意味はコンテキストに大きく依存する。外側の `try` に応じて異なる意味になる。

- 例外は参照透過性を失わせ、コンテキストへの依存をもたらす
  - 例外ベースの複雑なコードを可能にしてしまう
- 例外は型安全ではない 
  - `failingFn: Int => Int` の型からは、例外が発生することがわからない
    - コンパイラが例外処理を呼び出し元に強制するわけではないため、例外は実行時まで検出されない

例外を利用すれば、**エラー処理ロジックを一本化する**ことが可能となり、コードベース全体にそうしたロジックが散らばることがなくなる。

## 例外に代わる手法
部分関数 maen を定義し、例外をしようする一例を見てみる。

以下の方法では様々な問題点がある。
- mean
  - エラーが隠れている（Seq[Double] => Doubleからエラーが返されることを読み取れない） 
  - 呼び出し元が検証例外のボイラープレートだらけになる
  - 多相コードに適用できない（null を返したくても非プリミティブでしか使えない）
  - 呼び出し規則が呼び出し元に要求される
    - 適切に利用するには、mean を呼び出して結果を利用するだけでは不十分になる
- mean2
  - 返される型が `Double` のみになり汎用性に欠ける
    - `mean2(Seq(0.0, 0.0, 0.0), 0.0) == mean2(Seq.empty[Double], 0.0)` を違う意味として扱えなくなる（全ての平均が `0.0` なのか、そもそもリストの値がないのか）
```scala
object NotOption {

  def mean(seq: Seq[Double]): Double =
    if  (seq.isEmpty) throw new ArithmeticException("空のリストです")
    else seq.sum / seq.length

  def mean2(seq: Seq[Double], onEmpty: Double): Double =
    if  (seq.isEmpty) onEmpty
    else seq.sum / seq.length
}
```

### Option データ型
`mean` の問題の解決策は、問題への答えが常にあるとは限らないことを戻り値の型で表す。これは、エラー処理戦略を呼び出し元に委ねると考えることができる。

ここでは`Option` という新しい型を作成する。
`Option` には、定義可能なケースと、未定義となるケースがある。

定義可能なケースは `Some` となり、未定義となるケースは `None` となる。
```scala
sealed abstract class Option[+A]
case class  Some[A](v: A) extends Option[A]
case object None          extends Option[Nothing]

object Option {

  def mean(seq: Seq[Double]): Option[Double] =
    if (seq.isEmpty) None
    else             Some(seq.sum / seq.length)
}
```

呼び出し側にエラー処理戦略を委ねる
```scala
Option.mean(empty) match {
  case Some(v) => println(v)
  case None    => println("リストの値が見つかりません")
}
```

#### 無効な入力への応答
`Option` 型を使用しない場合

`0` 未満の数値を無効な値とする場合も、`Int` として返す。
無効な値かわかりづらく、呼び出し元が正しく処理できているか、コンパイラがチェックできない。
- `Int => Int`
  -  0 => 0
  -  1 => 1
  - -1 => -1

`Option` 型を使用した場合

`0` 未満のものは全て `None` として返す。
コンパイラがエラーの可能性を呼び出し元に明治的に処理させることができる。
- Int => Option[Int]
  -  0 => Some(0)
  -  1 => Some(1)
  - -1 => None

