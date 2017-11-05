/*
 * Copyright 2016 CarloMicieli
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package io.hascalator
package typeclasses

import Prelude._
import io.hascalator.data.{ Just, None }

import scala.{ Stream, PartialFunction }
import scala.annotation.{ tailrec, implicitNotFound }

/** Typeclass `Enum` defines operations on sequentially ordered types.
  *
  * Instances for this typeclasses can produce ''ranges'' of values:
  * - ''eagerly evaluated'': `enumFromTo` and `enumFromThenTo`
  * - ''lazily evaluated'': `enumFrom` and `enumFromThen`
  *
  * The minimal complete definition includes `toEnum` and `fromEnum`.
  *
  * @tparam A the instance data type
  * @author Carlo Micieli
  * @since 0.0.1
  */
@implicitNotFound("The type ${A} was not made an instance of the Enum type class.")
trait Enum[A] extends Any {
  /** Convert from an `Int` to the corresponding `A` value.
    *
    * For any type that is an instance of type class `Enum`, the following should hold:
    * - `toEnum` should returns `None` if the result value is not representable in the result type.
    *
    * @param x the element id
    * @return `Just(el)` if the input is valid, `None` otherwise
    */
  def toEnum(x: Int): Maybe[A]

  /** Convert from an `A` value to the corresponding `Int`.
    * @param x the element to convert
    * @return the corresponding element id
    */
  def fromEnum(x: A): Int

  /** Returns the successor of a value.
    * @param x the current element
    * @return `Just(el)` if the element has a successor, `None` otherwise.
    */
  def succ(x: A): Maybe[A] = toEnum(fromEnum(x) + 1)

  /** Returns the predecessor of a value.
    * @param x the current element
    * @return `Just(el)` if the element has a predecessor, `None` otherwise.
    */
  def prev(x: A): Maybe[A] = toEnum(fromEnum(x) - 1)

  /** Returns a Stream of values for this Enum, starting from `from`.
    * @param from the starting a value
    * @return a Stream of values
    */
  def enumFrom(from: A): Stream[A] = {
    def nextS(x: A): Stream[A] = {
      succ(x) match {
        case Just(y) => enumFrom(y)
        case _       => Stream.empty[A]
      }
    }

    from #:: nextS(from)
  }

  /** Produce a Stream of values from the Enum
    * @param x the first element
    * @param y the second element
    * @return a Stream
    */
  def enumFromThen(x: A, y: A): Stream[A] = {
    val vX = fromEnum(x)
    val vY = fromEnum(y)
    val step = vY - vX

    def nextS(a: A): Stream[A] = {
      a #:: {
        toEnum(fromEnum(a) + step) match {
          case Just(e) => nextS(e)
          case _       => Stream.empty[A]
        }
      }
    }

    if (vX > vY) {
      Stream.empty[A]
    } else {
      x #:: nextS(y)
    }
  }

  /** It translates in a list with elements from `from` to `to`.
    * @param from the starting element
    * @param to the final element
    * @return a List
    */
  def enumFromTo(from: A, to: A): List[A] = {
    val eqInt = Eq[Int]
    val fromV = fromEnum(from)
    val toV = fromEnum(to)

    if (fromV > toV) {
      List.empty[A]
    } else {
      @tailrec
      def loop(x: A, out: List[A]): List[A] = {
        if (eqInt.eq(fromV, fromEnum(x))) {
          x :: out
        } else {
          prev(x) match {
            case Just(v) => loop(v, x :: out)
            case _       => x :: out
          }
        }
      }

      loop(to, List.empty[A])
    }
  }

  /** It translates in a list with elements from `x` to `bound`, with the given step.
    *
    * `List(x, y, ... bound)`
    *
    * @param x the first element
    * @param y the second element
    * @param bound the last element in the list
    * @return a list
    */
  def enumFromThenTo(x: A, y: A, bound: A): List[A] = {
    val fromV = fromEnum(x)
    val toV = fromEnum(bound)

    if (fromV > toV) {
      List.empty[A]
    } else {
      @tailrec
      def loop(a1: A, a2: A, out: List[A]): List[A] = {
        val x: Int = fromEnum(a1)
        val y: Int = fromEnum(a2)

        if (x > y) {
          List.empty[A]
        } else if (y > toV) {
          a1 :: out
        } else {
          toEnum(y + (y - x)) match {
            case Just(z) => loop(a2, z, a1 :: out)
            case None =>
              if (x < toV && y < toV) {
                a2 :: a1 :: out
              } else if (x < toV) {
                a1 :: out
              } else {
                out
              }
          }
        }
      }

      loop(x, y, List.empty[A]).reverse
    }
  }
}

object Enum {
  def apply[A](implicit ev: Enum[A]): Enum[A] = ev

  implicit val boolean2Enum: Enum[Boolean] =
    newInstance[Boolean](b => if (b) 1 else 0)({
      case 1 => true
      case 0 => false
    })

  implicit val int2Enum: Enum[Int] =
    newInstance[Int](id)({ case x: Int => x })

  implicit val char2Enum: Enum[Char] =
    newInstance[Char](_.toInt)({
      case n if n >= scala.Char.MinValue.toInt && n <= scala.Char.MaxValue.toInt => n.toChar
    })

  implicit val float2Enum: Enum[Float] =
    newInstance[Float](_.toInt)({ case n => n.toFloat })

  implicit val double2Enum: Enum[Double] =
    newInstance[Double](_.toInt)({ case n => n.toDouble })

  private def newInstance[A](from: A => Int)(to: PartialFunction[Int, A]): Enum[A] = {
    new Enum[A] {
      override def fromEnum(x: A): Int = from(x)
      override def toEnum(x: Int): Maybe[A] = {
        if (to.isDefinedAt(x)) {
          Maybe.just(to(x))
        } else {
          Maybe.none
        }
      }
    }
  }
}