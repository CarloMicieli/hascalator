/*
 * Copyright 2016 Carlo Micieli
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

import scala.annotation.implicitNotFound
import scala.util.control.NoStackTrace
import scala.{ Tuple2, inline }

/** The Prelude: a standard module.
  *
  * @author Carlo Micieli
  * @since 0.0.1
  */
object Prelude {
  type tailrec = scala.annotation.tailrec

  type Any = scala.Any
  type AnyVal = scala.AnyVal
  type AnyRef = scala.AnyRef
  type Nothing = scala.Nothing
  type Unit = scala.Unit

  type Exception = java.lang.Exception

  type StringContext = scala.StringContext
  final val StringContext = scala.StringContext

  type Seq[+A] = scala.collection.immutable.Seq[A]
  val Seq = scala.collection.immutable.Seq

  type Traversable[A] = scala.collection.immutable.Traversable[A]

  type String = java.lang.String
  type Boolean = scala.Boolean
  type Byte = scala.Byte
  type Short = scala.Short
  type Char = scala.Char
  type Int = scala.Int
  type Integer = scala.math.BigInt
  type Long = scala.Long
  type Float = scala.Float
  type Double = scala.Double

  final val Boolean = scala.Boolean
  final val Byte = scala.Byte
  final val Short = scala.Short
  final val Char = scala.Char
  final val Int = scala.Int
  final val Integer = scala.math.BigInt
  final val Long = scala.Long
  final val Float = scala.Float
  final val Double = scala.Double

  type Maybe[+A] = data.Maybe[A]
  val Maybe = io.hascalator.data.Maybe

  type Either[+A, +B] = data.Either[A, B]
  val Either = io.hascalator.data.Either

  type List[+A] = data.List[A]
  val List = io.hascalator.data.List

  type Ordering = io.hascalator.typeclasses.Ordering
  val Ordering = io.hascalator.typeclasses.Ordering

  type Show[A] = io.hascalator.typeclasses.Show[A]
  val Show = io.hascalator.typeclasses.Show
  type Eq[A] = io.hascalator.typeclasses.Eq[A]
  val Eq = io.hascalator.typeclasses.Eq
  type Ord[A] = io.hascalator.typeclasses.Ord[A]
  val Ord = io.hascalator.typeclasses.Ord
  type Enum[A] = io.hascalator.typeclasses.Enum[A]
  val Enum = io.hascalator.typeclasses.Enum
  type Bounded[A] = io.hascalator.typeclasses.Bounded[A]
  val Bounded = io.hascalator.typeclasses.Bounded
  type Num[A] = io.hascalator.typeclasses.Num[A]
  val Num = io.hascalator.typeclasses.Num
  type Real[A] = io.hascalator.typeclasses.Real[A]
  val Real = io.hascalator.typeclasses.Real
  type Integral[A] = io.hascalator.typeclasses.Integral[A]
  val Integral = io.hascalator.typeclasses.Integral
  type Fractional[A] = io.hascalator.typeclasses.Fractional[A]
  val Fractional = io.hascalator.typeclasses.Fractional

  /** The identity function
    *
    * @tparam A the function type
    * @return the identity function
    */
  def id[A]: A => A = {
    x => x
  }

  /** `const(x)` is a unary function which evaluates to `x` for all inputs.
    *
    * @param const the const value the function will always return
    * @tparam A the output type
    * @tparam B the input type
    * @return a constant function
    */
  def const[A, B](const: A): B => A = {
    _ => const
  }

  /** is a type-restricted version of [[const]].
    * @param x the const value the function will always return
    * @tparam A the output type
    * @return a constant function
    */
  @inline final def asTypeOf[A](x: A): A => A = const(x)

  /** `flip(f)` takes its (first) two arguments in the reverse order of `f`.
    *
    * @param f a binary function
    * @tparam A the first argument type
    * @tparam B the second argument type
    * @tparam C the return type
    * @return a function with the two arguments reversed
    */
  def flip[A, B, C](f: (A, B) => C): (B, A) => C = (b, a) => f(a, b)

  /** `until p f` yields the result of applying `f` until `p` holds.
    *
    * @param p the termination condition
    * @param f the function to compute the next value
    * @param z the initial value
    * @tparam A
    * @return the result
    */
  @tailrec
  final def until[A](p: A => Boolean)(f: A => A)(z: A): A = {
    if (p(z)) {
      z
    } else {
      until(p)(f)(f(z))
    }
  }

  /** stops execution and displays an error message.
    * @param msg the error message
    * @tparam A the return typeù
    * @return
    */
  def error[A](msg: String): A = {
    throw new ApplicationException(s"*** Exception: $msg")
  }

  /** A variant of [[error]] that does not produce a stack trace.
    * @param msg the error message
    * @tparam A the return type
    * @return
    */
  def errorWithoutStackTrace[A](msg: String): A = {
    throw new ApplicationException(s"*** Exception: $msg") with NoStackTrace
  }

  /** A special case of [[error]]. It is expected that compilers will recognize
    * this and insert error messages which are more appropriate to the context
    * in which undefined appears.
    * @tparam A basically any type
    * @return
    */
  @inline final def undefined[A]: A = throw new scala.NotImplementedError

  /** Extract the first component of a pair.
    * @param p a pair
    * @return the first component
    */
  @inline final def fst[A, B](p: (A, B)): A = p._1

  /** Extract the second component of a pair.
    * @param p a pair
    * @return the second component
    */
  @inline final def snd[A, B](p: (A, B)): B = p._2

  // Definitions from Scala.Predef

  @implicitNotFound(msg = "Cannot prove that ${From} <:< ${To}.")
  sealed abstract class <:<[-From, +To] extends (From => To) with scala.Serializable
  private[this] final val singleton_<:< = new <:<[Any, Any] { def apply(x: Any): Any = x }

  @implicitNotFound(msg = "Cannot prove that ${From} =:= ${To}.")
  sealed abstract class =:=[From, To] extends (From => To) with scala.Serializable
  private[this] final val singleton_=:= = new =:=[Any, Any] { def apply(x: Any): Any = x }
  object =:= {
    implicit def tpEquals[A]: A =:= A = singleton_=:=.asInstanceOf[A =:= A]
  }

  @inline final def require(requirement: Boolean): Unit = {
    scala.Predef.require(requirement)
  }

  @inline final def require(requirement: Boolean, message: => Any): Unit = {
    scala.Predef.require(requirement, message)
  }

  @inline def implicitly[T](implicit e: T): T = e

  implicit class Function2Flip[A, B, C](val f: (A, B) => C) extends AnyVal {
    def flip: (B, A) => C = Prelude.flip(f)
  }

  implicit class PredicateNegate[A](val p: A => Boolean) extends AnyVal {
    def negate: A => Boolean = a => !p(a)
  }

  def print(x: Any) = scala.Console.print(x)
  def println() = scala.Console.println()
  def println(x: Any) = scala.Console.println(x)

  implicit class ArrowAssoc[A](private val a: A) extends AnyVal {
    @inline def -> [B](b: B): Tuple2[A, B] = Tuple2(a, b)
  }
}