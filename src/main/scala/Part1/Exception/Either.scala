package Part1.Exception

sealed abstract class Either[+E, +A] {

  def map[B](f: A => B): Either[E, B]
  def flatMap[EE >: E, B](f: A => Either[EE, B]): Either[EE, B]
  def orElse[EE >: E, B >: A](b: => Either[EE , B]): Either[EE, B]
  def map2[EE >: E, B, C](b: Either[EE, B])(f: (A, B) => C): Either[EE, C]
  def map2E[EE >: E, B, C](b: Either[EE, B])(f: (A, B) => C): Either[List[EE], C]
}
case class Left [E](v: E) extends Either[E,  Nothing] {

  override def map[B](f: Nothing => B): Either[E, B] = Left(v)

  override def flatMap[EE >: E, B](f: Nothing => Either[EE, B]): Either[EE, B] = Left(v)

  override def orElse[EE >: E, B >: Nothing](b: => Either[EE, B]): Either[EE, B] = b

  override def map2[EE >: E, B, C](before: Either[EE, B])(f: (Nothing, B) => C): Either[EE, C] = Left(v)

  override def map2E[EE >: E, B, C](b: Either[EE, B])(f: (Nothing, B) => C): Either[List[EE], C] =
    b match {
      case Right(_) => Left(List(v))
      case Left(e)  => Left(List(v, e))
    }
}
case class Right[A](v: A) extends Either[Nothing, A] {

  override def map[B](f: A => B): Either[Nothing, B] = Right(f(v))

  override def flatMap[EE >: Nothing, B](f: A => Either[EE, B]): Either[EE, B] = f(v)

  override def orElse[EE >: Nothing, B >: A](b: => Either[EE, B]): Either[EE, B] = Right(v)

  override def map2[EE >: Nothing, B, C](before: Either[EE, B])(f: (A, B) => C): Either[EE, C] =
    before match {
      case Right(b) => Right(f(v, b))
      case Left(e)  => Left(e)
    }

  override def map2E[EE >: Nothing, B, C](b: Either[EE, B])(f: (A, B) => C): Either[List[EE], C] =
    b match {
      case Right(r) => Right(f(v, r))
      case Left(e)  => Left(List(e))
    }
}

object Either {

  def mean(ds: Seq[Double]): Either[String, Double] =
    if (ds.isEmpty) Left("Error: mean of empty list")
    else            Right(ds.sum / ds.length)

  def safeDiv(x: Int, y: String): Either[Exception, Int] =
    try Right(x / y.toInt)
    catch { case e: Exception => Left(e) }

  def sequence[E, A](eList: List[Either[E, A]]): Either[E, List[A]] = {
    val (rights, left) = eList.foldLeft(List.empty[A], None: Option[E]) { case ((rights, left), either) =>
      (left.isEmpty, either) match {
        case (true, Right(v)) =>
          val list = rights ++ List(v)
          (list, left)
        case (_, Left(v))     =>
          val leftOpt = if (left.isDefined) left else Some(v)
          (rights, leftOpt)
        case (_, _)           => (rights, left)
      }
    }
    left match {
      case Some(v) => Left(v)
      case None    => Right(rights)
    }
  }

  def traverse[E, A, B](eList: List[Either[E, A]])(f: A => B): Either[E, List[B]] = {
    val (leftOpt, rightList) = eList.foldLeft(None: Option[E], List.empty[B]) { case ((left, rights), either) =>
      (left.isEmpty, either) match {
        case (true, Right(v)) =>
          (left, rights :+ f(v))
        case (_, Left(v)) =>
          val leftOpt = if (left.isDefined) left else Some(v)
          (leftOpt, rights)
        case (_, _) =>
          (left, rights)
      }
    }
    leftOpt match {
      case Some(v) => Left(v)
      case None    => Right(rightList)
    }
  }
}
