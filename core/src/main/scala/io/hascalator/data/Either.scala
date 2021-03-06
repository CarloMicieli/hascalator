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
package data

import Prelude._

/** The `Either` type represents values with two possibilities: a value of type `Either[A,B]` is
  * either `Left(a)` or `Right(b)`.
  *
  * The `Either` type is sometimes used to represent a value which is either correct or an error;
  * by convention, the `Left` constructor is used to hold an error value and the `Right` constructor
  * is used to hold a correct value (mnemonic: "right" also means "correct").
  * +
  * @tparam A the left data type
  * @tparam B the right data type
  *
  * @author Carlo Micieli
  * @since 0.0.1
  */
sealed trait Either[+A, +B] {

  /** Returns `true` if `this` is a ''Left'' value; `false` otherwise.
    * @return `true` if `this` is a ''Left'' value; `false` otherwise
    */
  def isLeft: Boolean

  /** Returns `true` if `this` is a ''Right'' value; `false` otherwise.
    * @return `true` if `this` is a ''Right'' value; `false` otherwise
    */
  def isRight: Boolean = !isLeft

  /** Returns the value contained if `this` is a ''Right'' value; it
    * throws an exception otherwise.
    *
    * @return the value contained in the ''Right''
    */
  private[data] def get: B

  /** Returns the contained value if `this` is a ''Right''; it returns
    * the provided `default` otherwise.
    *
    * @usecase def getOrElse(default: => B): B
    * @inheritdoc
    * @param default the default value
    * @tparam B1 the ''Right'' element type
    * @return the ''Right'' value; or the `default` otherwise
    */
  def getOrElse[B1 >: B](default: => B1): B1 = {
    if (isRight) {
      get
    } else {
      default
    }
  }

  /** Applies the given function `f` to the contained value if `this` is a ''Right'';
    * does nothing if `this` is a ''Left''.
    *
    * @usecase def foreach(f: B => Unit): Unit
    * @inheritdoc
    * @param f the function to apply
    * @tparam U
    */
  def foreach[U](f: B => U): Unit = {
    if (isRight) {
      val discarded = f(get)
    } else {
      ()
    }
  }

  /** Case analysis for the `Either` type. If the value is a ''Left(a)'', apply the first
    * function to ''a''; if it is ''Right(b)'', apply the second function to ''b''.
    *
    * @param left the function to map ''Left'' values
    * @param right the function to map ''Right'' values
    * @tparam C the resulting data type
    * @return the result applying either `left` or `right` function
    */
  def either[C](left: A => C)(right: B => C): C = {
    this match {
      case Right(v) => right(v)
      case Left(v)  => left(v)
    }
  }

  /** Maps the given function to this `Either`'s value if it is a ''Right'' or returns this if it is a ''Left''.
    * @param f the function to apply
    * @tparam B1 the resulting value type
    * @return if this is a ''Right'', the result of applying the given function to the contained value
    * wrapped in a ''Right''; a ''Left'' otherwise
    */
  def map[B1](f: B => B1): Either[A, B1] = {
    this match {
      case Left(_)  => this.asInstanceOf[Either[A, B1]]
      case Right(v) => Right(f(v))
    }
  }

  /** Returns the given function applied to the value contained in this `Either` if it is a ''Right'',
    * or returns this if it is a ''Left''.
    *
    * @usecase def flatMap[B1](f: B => Either[A, B1]): Either[A, B1]
    * @inheritdoc
    * @param f the function to apply
    * @tparam A1 the ''Left'' type
    * @tparam B1 the ''Right'' type
    * @return if this is a ''Right'', the result of applying the given function to the contained value
    * wrapped in a ''Right''; a ''Left'' otherwise
    */
  def flatMap[A1 >: A, B1](f: B => Either[A1, B1]): Either[A1, B1] = {
    this match {
      case Left(_)  => this.asInstanceOf[Either[A1, B1]]
      case Right(v) => f(v)
    }
  }

  /** It accumulate two `Either`s together. If both are ''Right'', you'll get a ''Right'' tuple containing
    * both original ''Right'' values. Otherwise, you'll get a ''Left'' containing a tuple with the
    * original ''Left'' values.
    *
    * @param that other `Either` value
    * @tparam A1 the other ''Left'' type
    * @tparam B1 the other ''Right'' type
    * @return a new `Either` value combining the original values
    */
  def zip[A1 >: A, B1](that: Either[A1, B1]): Either[List[A1], (B, B1)] = {
    (this, that) match {
      case (Right(a), Right(b)) => Right((a, b))
      case (Left(a), Left(b))   => Left(List(a, b))
      case (Left(x), _)         => Left(List(x))
      case (_, Left(y))         => Left(List(y))
    }
  }

  /** Returns `true` if this `Either` is a ''Right'' and the predicate `p` returns
    * `true` when applied to this ''Right'' value.
    *
    * @param p the predicate to match
    * @return the result of applying the passed predicate `p` to the ''Right'' value, if this is a ''Right''; `false` otherwise
    */
  def exists(p: B => Boolean): Boolean = {
    if (isRight) {
      p(get)
    } else {
      false
    }
  }

  /** Returns an `Either` with the ''Right'' and ''Left'' types swapped: ''Left'' becomes ''Right'' and ''Right''
    * becomes ''Left''.
    *
    * @return a swapped `Either` value
    */
  def swap: Either[B, A] = this match {
    case Right(g) => Left(g)
    case Left(b)  => Right(b)
  }

  /** Returns a `Just` value with the element contained in a ''Right'';
    * it simply return `None` otherwise.
    *
    * @usecase def toMaybe: Maybe[B]
    * @inheritdoc
    * @tparam B1 the element type
    * @return a `Just` if this value is right; a `None` otherwise
    */
  def toMaybe[B1 >: B]: Maybe[B1] = {
    if (isRight) {
      Maybe.just(get)
    } else {
      Maybe.none[B1]
    }
  }
}

object Either extends EitherInstances {
  /** Produces a right when `p` is true, a left value otherwise
    * @param p the predicate
    * @param left the left value
    * @param right the right value
    * @tparam A
    * @tparam B
    * @return
    */
  def cond[A, B](p: Boolean)(left: => A, right: => B): Either[A, B] = {
    if (p) {
      Either.right(right)
    } else {
      Either.left(left)
    }
  }

  /** Build a new ''Left'' value.
    *
    * @param v the wrapped value
    * @tparam A the ''Left'' data type
    * @tparam B the ''Right'' data type
    * @return the left value
    */
  def left[A, B](v: A): Either[A, B] = Left(v)

  /** Build a new ''Right'' value.
    *
    * @param v the wrapped value
    * @tparam A the ''Left'' data type
    * @tparam B the ''Right'' data type
    * @return the right value
    */
  def right[A, B](v: B): Either[A, B] = Right(v)

  /** Extracts from a list of `Either` all the Left elements. All the Left elements are extracted in order.
    * @param xs a list of `Either` values
    * @tparam A
    * @tparam B
    * @return
    */
  def lefts[A, B](xs: List[Either[A, B]]): List[A] = {
    for { Left(x) <- xs } yield x
  }

  /** Extracts from a list of Either all the Right elements. All the Right elements are extracted in order.
    * @param xs
    * @tparam A
    * @tparam B
    * @return
    */
  def rights[A, B](xs: List[Either[A, B]]): List[B] = {
    for { Right(x) <- xs } yield x
  }

  /** Partitions a list of Either into two lists. All the Left elements are extracted, in order, to the first component
    * of the output. Similarly the Right elements are extracted to the second component of the output.
    * @param xs
    * @tparam A
    * @tparam B
    * @return
    */
  def partitionEithers[A, B](xs: List[Either[A, B]]): (List[A], List[B]) = {
    val builderLeft = new ListBuilder[A]
    val builderRight = new ListBuilder[B]

    xs.foreach {
      case Left(x)  => builderLeft += x
      case Right(x) => builderRight += x
    }

    (builderLeft.result(), builderRight.result())
  }
}

trait EitherInstances {
  implicit def toShowEither[A: Show, B: Show]: Show[Either[A, B]] = Show.fromFunction {
    x =>
      {
        val l = (a: A) => s"Left(${implicitly[Show[A]].show(a)})"
        val r = (b: B) => s"Right(${implicitly[Show[B]].show(b)})"
        x.either(l)(r)
      }
  }

  implicit def toOrdEither[A, B](implicit ordA: Ord[A], ordB: Ord[B]): Ord[Either[A, B]] = Ord.fromFunction {
    (x, y) =>
      {
        (x, y) match {
          case (Right(a), Right(b)) => ordB.compare(a, b)
          case (Left(_), Right(_))  => Ordering.LT
          case (Right(_), Left(_))  => Ordering.GT
          case (Left(a), Left(b))   => ordA.compare(a, b)
        }
      }
  }
}

private[this] case class Left[A] private (value: A) extends Either[A, Nothing] {
  override def isLeft: Boolean = true
  override def get: Nothing = error("Left.get: this value is a Left")
}

private[this] case class Right[B] private (value: B) extends Either[Nothing, B] {
  override def isLeft: Boolean = false
  override def get: B = value
}
