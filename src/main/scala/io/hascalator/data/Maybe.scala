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

/** The `Maybe` type encapsulates an optional value. A value of type `Maybe[A]`
  * either contains a value of type a (represented as `Just[A]`), or it is
  * empty (represented as `None`).
  *
  * @tparam A the element type
  *
  * @author Carlo Micieli
  * @since 0.0.1
  */
sealed trait Maybe[+A] {
  self =>
  /** Return the contained value if `this` is a `Just[_]` value,
    * otherwise it will throw a `NoSuchElementException`.
    * @return the contained value if this is a `Just[_]`; throws an exception otherwise
    */
  def get: A

  /** Return the contained value if `this` is a `Just` value,
    * otherwise returns the `default` value.
    *
    * @usecase def getOrElse(default: => A): A
    * @inheritdoc
    * @param default the value to be returned when `this` is a `Bad` value
    * @tparam A1 the element type
    * @return
    */
  def getOrElse[A1 >: A](default: => A1): A1 = {
    if (isDefined) get else default
  }

  /** It returns the first `Maybe` if it’s defined; otherwise, it returns
    * the second `Maybe`.
    *
    * @usecase def orElse(default: => Maybe[A]): Maybe[A]
    * @inheritdoc
    * @param that the value returned whether `this` is a `None`
    * @tparam A1 the element type
    * @return
    */
  def orElse[A1 >: A](that: => Maybe[A1]): Maybe[A1] = {
    if (isDefined) this else that
  }

  /** Returns `true` iff its argument is of the form `Just[_]`.
    * @return `true` if this value is a `Just[_]`; `false` otherwise
    */
  def isDefined: Boolean

  /** Returns `true` iff its argument is of the form `None`; return `false` otherwise.
    * @return `true` if this value is a `None`; `false` otherwise
    */
  def isEmpty: Boolean = !isDefined

  /** Apply the function `f` if `this` is a `Just[_]` value, it doesn't
    * do anything otherwise.
    *
    * @usecase def foreach(f: A => Unit): Unit
    * @inheritdoc
    * @param f the function to apply to the contained value
    * @tparam U
    */
  def foreach[U](f: A => U): Unit =
    if (isDefined) {
      val res = f(get)
    }

  /** Returns a `Just[_]` containing the result of applying `f` to this
    * `Maybe`'s value if this is a  `Just[_]`; otherwise it simply returns a `None`.
    *
    * @usecase def map(f: A => A): Maybe[A]
    * @inheritdoc
    * @param f the function to apply
    * @tparam A1
    * @return a new `Maybe` where the function `f` has been applied to the value.
    */
  def map[A1](f: A => A1): Maybe[A1] =
    if (isDefined) Just(f(get)) else None

  /** Returns a `Just[_]` containing the result of applying `f` to this
    * `Maybe`'s value if this is a  `Just[_]`; otherwise it simply returns a `None`.
    *
    * @usecase def flatMap(f: A => Maybe[A]): Maybe[A]
    * @inheritdoc
    * @param f the function to apply
    * @tparam A1
    * @return a new `Maybe` where the function `f` has been applied to the value.
    */
  def flatMap[A1](f: A => Maybe[A1]): Maybe[A1] =
    if (isDefined) f(get) else None

  /** Apply the function `f` to the contained value if this is a `Just`, simply returns
    * `orElse` otherwise.
    * @param f the function to apply
    * @param orElse the value returned if this is a `None`
    * @tparam A1 the resulting type
    * @return a new `Maybe` after applying `f` if this is a `Just` value, `orElse` otherwise
    */
  def mapOrElse[A1](f: A => A1)(orElse: => Maybe[A1]): Maybe[A1] = map(f).orElse(orElse)

  /** Returns the value after the function `f` has been applied to the wrapped value if this is a
    * `Just`, simply returns `orElse` otherwise.
    * @param f the function to apply
    * @param orElse the value returned for `None` values
    * @tparam B the resulting type
    * @return
    */
  def fold[B](f: A => B)(orElse: => B): B = map(f).getOrElse(orElse)

  /** Returns this `Maybe` if it is nonempty and applying the predicate `p` to
    * this `Maybe`'s value returns `true`.
    * @param p the predicate to apply
    * @return
    */
  def filter(p: A => Boolean): Maybe[A] =
    if (isDefined && p(get)) this else None

  /** Returns an empty list when given `None` or a singleton list when given a `Just[_]`.
    * @return a list
    */
  def toList: List[A] = fold(List(_))(List.empty[A])

  def withFilter(p: A => Boolean): WithFilter = new WithFilter(p)

  class WithFilter(p: A => Boolean) {
    def flatMap[B](f: A => Maybe[B]): Maybe[B] = self filter p flatMap f
    def foreach[U](f: A => U): Unit = self filter p foreach f
    def map[B](f: A => B): Maybe[B] = self filter p map f
    def withFilter(q: A => Boolean): WithFilter = new WithFilter(x => p(x) && q(x))
  }
}

object Maybe extends MaybeInstances {
  /** Creates a new empty value.
    *
    * @tparam A
    * @return
    */
  def none[A]: Maybe[A] = None

  def just[A](x: A): Maybe[A] = Just(x)

  /** Creates a new `Just` value whether the provided `x` is not `null`; returns a `None` otherwise.
    *
    * @param x the value
    * @tparam A the value type
    * @return a new `Maybe` value
    */
  def apply[A](x: A): Maybe[A] = {
    if (x == null) {
      None
    } else {
      Just(x)
    }
  }

  /** It takes a list of `Maybe`s and returns a list of all the `Just` values.
    *
    * @param xs
    * @tparam A
    * @return
    */
  def catMaybes[A](xs: List[Maybe[A]]): List[A] = {
    for { Just(x) <- xs } yield x
  }
}

trait MaybeInstances {
  implicit def toShowMaybe[A: Show]: Show[Maybe[A]] = Show {
    (x: Maybe[A]) =>
      x.map(v => implicitly[Show[A]].show(v)).toString
  }

  implicit def toOrdMaybe[A](implicit ordA: Ord[A]): Ord[Maybe[A]] = Ord {
    (x: Maybe[A], y: Maybe[A]) =>
      {
        (x, y) match {
          case (None, None)       => Ordering.EQ
          case (None, Just(_))    => Ordering.LT
          case (Just(_), None)    => Ordering.GT
          case (Just(a), Just(b)) => ordA.compare(a, b)
        }
      }
  }
}

final case class Just[A] private (get: A) extends Maybe[A] {
  def isDefined: Boolean = true
}

case object None extends Maybe[Nothing] {
  def get: Nothing = error("Maybe.get: a value doesn't exist")
  def isDefined: Boolean = false
}
