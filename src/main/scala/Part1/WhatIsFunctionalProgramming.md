## 1. 関数型プログラミングとは
関数型プログラミングでは、純粋関数だけを使用してプログラムを子移築することを前提としている。

### 1-2. 純粋関数とは
純粋関数とは、副作用のない関数のことを指す。
- 変数を変更する
- データ構造を直接変更する
- オブジェクトのフィールドを設定する
- 例外をスローする、またはエラーで停止する
- コンスールに出力する、またはユーザー入力を読み取る
- ファイルを読み取る、またはファイルに書き込む
- 画面上に描画する

関数型プログラミングはプログラムの書き方を制約するものである。ただし、表現可能なプログラムの「種類」を制約するものではない。

純粋関数は式が参照透過であるなど、書籍では難しいことを長々書いている。自分の理解で言うと、参照透過性は「関数の式 = 関数の結果」でければならないということだと思う。

Java の StringBuilder には副作用があり、参照透過性が破壊されている。append したあとに代入している変数の値をみると、全ての変数が同じ値を持っている。Java の StringBuilder はシングルトンであるためミュータブルであり、append の前後の処理を意識しなければならない。

今回作成したオリジナルの StringBuilder は、参照透過性（関数の式 = 関数の結果）を意識して実装した。 append 後に代入した変数を出力すると、append で返した値を処理の順序に従って出力している。

```Scala
// 副作用のない StringBuilder を作成
// Original StringBuilder
case class StringBuilder(string: String = "") {
  // Add string
  def append(str: String): StringBuilder =
    StringBuilder(string + str)
}

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
```
