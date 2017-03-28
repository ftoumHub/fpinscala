package fpinscala.exercises.ch10monoids

import org.scalacheck._
import org.scalacheck.Prop._

import Monoid._

object MonoidTest extends Properties("Monoid") {

  property("stringMonoid laws should hold") = forAll {
    (a1: String, a2: String, a3: String) => {
      stringMonoid.op(stringMonoid.op(a1, a2), a3) ==
        stringMonoid.op(a1, stringMonoid.op(a2, a3)) &&
      stringMonoid.op(a1, stringMonoid.zero) == a1 &&
      stringMonoid.op(a2, stringMonoid.zero) == a2 &&
      stringMonoid.op(a3, stringMonoid.zero) == a3
    }
  }

  property("listMonoid laws should hold") = forAll {
    (a1: List[String], a2: List[String], a3: List[String]) => {
      listMonoid.op(listMonoid.op(a1, a2), a3) ==
        listMonoid.op(a1, listMonoid.op(a2, a3)) &&
      listMonoid.op(a1, listMonoid.zero) == a1 &&
      listMonoid.op(a2, listMonoid.zero) == a2 &&
      listMonoid.op(a3, listMonoid.zero) == a3
    }
  }

  property("intAddition laws should hold") = forAll {
    (a1: Int, a2: Int, a3: Int) => {
      intAddition.op(intAddition.op(a1, a2), a3) ==
        intAddition.op(a1, intAddition.op(a2, a3)) &&
      intAddition.op(a1, intAddition.zero) == a1 &&
      intAddition.op(a2, intAddition.zero) == a2 &&
      intAddition.op(a3, intAddition.zero) == a3
    }
  }

  property("intMultiplication laws should hold") = forAll {
    (a1: Int, a2: Int, a3: Int) => {
      intMultiplication.op(intMultiplication.op(a1, a2), a3) ==
        intMultiplication.op(a1, intMultiplication.op(a2, a3)) &&
      intMultiplication.op(a1, intMultiplication.zero) == a1 &&
      intMultiplication.op(a2, intMultiplication.zero) == a2 &&
      intMultiplication.op(a3, intMultiplication.zero) == a3
    }
  }

  property("booleanOr laws should hold") = forAll {
    (a1: Boolean, a2: Boolean, a3: Boolean) => {
      booleanOr.op(booleanOr.op(a1, a2), a3) ==
        booleanOr.op(a1, booleanOr.op(a2, a3)) &&
      booleanOr.op(a1, booleanOr.zero) == a1 &&
      booleanOr.op(a2, booleanOr.zero) == a2 &&
      booleanOr.op(a3, booleanOr.zero) == a3
    }
  }

  property("booleanAnd laws should hold") = forAll {
    (a1: Boolean, a2: Boolean, a3: Boolean) => {
      booleanAnd.op(booleanAnd.op(a1, a2), a3) ==
        booleanAnd.op(a1, booleanAnd.op(a2, a3)) &&
      booleanAnd.op(a1, booleanAnd.zero) == a1 &&
      booleanAnd.op(a2, booleanAnd.zero) == a2 &&
      booleanAnd.op(a3, booleanAnd.zero) == a3
    }
  }

  /**
  property("monoidLaws should hold for the monoids") = check {
    monoidLaws(stringMonoid, Gen.alphaStr)
  }
  */
}
