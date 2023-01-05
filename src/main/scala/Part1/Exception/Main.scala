package Part1.Exception

object Main {

  def main(args: Array[String]): Unit = {
    println(Faile.failing2(100))
    // Faile.failing(100) Error

    val seq: Seq[Double] = Seq(
      1.4,
      55.7,
      333.3,
      22.22,
      222.1,
      1.5
    )
    val empty: Seq[Double] = Seq.empty[Double]

    println(NotOption.mean(seq))
    // Option.mean(empty) Error

    // 呼び出し元がリストが空の場合の対処方法を知っておかなければならず、返される型が Double に限定される
    println(NotOption.mean2(empty, 0.0))

    //------------------------------------------------
    // Option

    Option.mean(empty) match {
      case Some(v) => println(v)
      case None    => println("リストの値が見つかりません")
    }

    Option.variance(seq) match {
      case Some(v) => println(v)
      case None    => println("リストが見つかりません")
    }

    val abs = Option.lift(math.abs)
    val v   = Some(-100)
    println(abs(v))

    val listOpt: List[Option[String]] = List(
      Some("A"),
      Some("B"),
      Some("AB"),
      Some("O"),
    )
    val listOpt2: List[Option[String]] = List(
      Some("A"),
      None
    )
    println(Option.sequence(listOpt))
    println(Option.sequence(listOpt2))

    val toString: Option[String] => String = (opt: Option[String]) => opt.toString + "__@github.com"
    println(Option.traverse(listOpt)(toString))

    //--------------------------------------------
    // Either
    println(Either.mean(seq))
    println(Either.mean(empty))

    println(Either.safeDiv(100, "A"))

    val right: Either[Abstract, Int] = Right(100)
    println(right.map(_ + 1000))
    println(right.flatMap(v => Right(v + 100000)))

    val toEnum = (x: Int) => x match {
      case  1  => Right(Abstract.Concrete1)
      case 10  => Right(Abstract.Concrete2)
      case 100 => Right(Abstract.Concrete3)
      case _   => Right(Abstract.ConcreteOther)
    }
    val enum = right.flatMap(toEnum)
    println(enum)

    val left: Either[String, Int] = Left("ERROR!!!")
    println(left.flatMap(toEnum))

    val enumEither:  Either[String, Abstract.Concrete1.type] = Right(Abstract.Concrete1)
    val enumEither1: Either[String, Abstract.Concrete1.type] = Right(Abstract.Concrete1)
    val enumEither2: Either[String, Abstract.Concrete1.type] = Left("ERROR: sequence function or traverse function test!")

    val enumList  = List(enumEither, enumEither1)
    val enumList2 = List(enumEither, enumEither2)

    println(Either.sequence(enumList))
    println(Either.sequence(enumList2))

    val toConcreteOther: Abstract.Concrete1.type => Abstract.ConcreteOther.type = c1 => Abstract.ConcreteOther

    println(Either.traverse(enumList) (toConcreteOther))
    println(Either.traverse(enumList2)(toConcreteOther))

    // map2 だと両方とも不正な値の場合、1つのエラーメッセージしか取得できない
    val emptyString: String = ""
    val invalidInt:  Int    = 1000
    println(mkName(emptyString).map2(mkAge(invalidInt))(Person(_, _)))                      // Left(ERROR: This string is empty!)
    println(mkAge(invalidInt).map2(mkName(emptyString))((x: Age, y: Name) => Person(y, x))) // Left(500)

    // 2つのエラーが出せるように修正せよ
    println(mkName(emptyString).map2E(mkAge(invalidInt))(Person(_, _)))                      // Left(List(ERROR: This string is empty!, 500))
    println(mkAge(invalidInt).map2E(mkName(emptyString))((x: Age, y: Name) => Person(y, x))) // Left(List(500, ERROR: This string is empty!))
  }

  sealed abstract class Abstract(v: Int)
  object Abstract {
    case object Concrete1     extends Abstract(1)
    case object Concrete2     extends Abstract(10)
    case object Concrete3     extends Abstract(100)
    case object ConcreteOther extends Abstract(9999)
  }

  sealed case class Name(v: String)
  sealed case class Age (v: Int)
  case class Person(name: Name, age: Age)

  def mkName(str: String): Either[String, Name] =
    if (str.isEmpty) Left("ERROR: This string is empty!")
    else Right(Name(str))

  def mkAge(num: Int): Either[Int, Age] =
    if (num < 0 || 120 < num) Left(500)
    else Right(Age(num))
}
