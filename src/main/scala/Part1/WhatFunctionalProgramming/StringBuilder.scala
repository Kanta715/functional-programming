package Part1.WhatFunctionalProgramming

// 副作用のない StringBuilder を作成
// Original StringBuilder
case class StringBuilder(string: String = "") {

  // Add string
  def append(str: String): StringBuilder =
    StringBuilder(string + str)

  override def toString: String = string
}
