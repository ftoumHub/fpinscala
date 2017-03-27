package fpinscala.exercises.ch09parsing

import scala.util.matching.Regex

import ReferenceTypes._


object ReferenceTypes {
  type Parser[+A] = ParseState => Result[A]

  case class ParseState(loc: Location) {
    def advanceBy(n: Int): ParseState = copy(loc = loc.copy(offset = loc.offset + n))
    def input: String = loc.input.substring(loc.offset)
    def slice(n: Int): String = loc.input.substring(loc.offset, loc.offset + n)
  }

  sealed trait Result[+A] {
    def extract: Either[ParseError, A] = this match {
      case Failure(e, _) => Left(e)
      case Success(a, _) => Right(a)
    }

    def addCommit(isCommitted: Boolean): Result[A] = this match {
      case Failure(e, c) => Failure(e, c || isCommitted)
      case _ => this
    }

    def advanceSuccess(n: Int): Result[A] = this match {
      case Success(a, l) => Success(a, l + n)
      case _ => this
    }
  }

  case class Success[+A](get: A, length: Int) extends Result[A]
  case class Failure(get: ParseError, isCommitted: Boolean) extends Result[Nothing]

  def firstNonMatchingIndex(s1: String, s2: String, offset: Int): Int = {
    def loop(idx: Int): Int = {
      val s1Idx = idx + offset
      val s2Idx = idx

      if (s2Idx >= s2.length) -1
      else if (s1Idx >= s1.length) s2Idx
      else {
        val charS1 = s1.charAt(s1Idx)
        val charS2 = s2.charAt(s2Idx)
        if (charS1 == charS2) loop(s2Idx + 1)
        else s2Idx
      }
    }
    loop(0)
  }
}

object Reference extends Parsers[Parser] {
  def run[A](p: Parser[A])(s: String): Either[ParseError, A] = {
    p(ParseState(Location(s))).extract
  }
  def succeed[A](a: A): Parser[A] = s => Success(a, 0)
  def or[A](p1: Parser[A], p2: Parser[A]): Parser[A] = s => {
    p1(s) match {
      case Failure(e, false) => p2(s)
      case success => success
    }
  }
  def flatMap[A, B](p: Parser[A])(f: A => Parser[B]): Parser[B] = s => {
    p(s) match {
      case Success(a, length) => f(a)(s.advanceBy(length)).addCommit(length != 0)
                                                          .advanceSuccess(length)
      case fail@Failure(_, _) => fail
    }
  }
  def string(s: String): Parser[String] = state => {
    val msg = "'" + s + "'"
    val i = firstNonMatchingIndex(state.loc.input, s, state.loc.offset)
    if (i == -1) Success(s, s.length)
    else Failure(state.loc.advanceBy(i).toError(msg), i != 0)
  }
  def regex(r: Regex): Parser[String] = state => {
    val msg = "regex " + r
    r.findPrefixOf(state.input) match {
      case None => Failure(state.loc.toError(msg), false)
      case Some(m) => Success(m, m.length)
    }
  }
  def slice[A](p: Parser[A]): Parser[String] = state => {
    p(state) match {
      case Success(_, n) => Success(state.slice(n), n)
      case f@Failure(_, _) => f
    }
  }
}
