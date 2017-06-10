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

/** Non-empty (and strict) list type.
  * @tparam A the element type
  * @author Carlo Micieli
  * @since 0.0.1
  */
sealed trait NonEmpty[A] {
  /** Prepend an element to the stream.
    * @param x the new element
    * @return a new list
    */
  def |:(x: A): NonEmpty[A] = NonEmptyCons(x, this.toList)

  /** Return the length of this NonEmpty list
    * @return list length
    */
  def length: Int = fold(_ => 1, (_, xs) => 1 + xs.length)

  /** Extract the first element of the list.
    * @return the head
    */
  def head: A = fold(x => x, (x, _) => x)

  /** Extract the possibly-empty tail of the NonEmpty list.
    * @return the tail
    */
  def tail: List[A] = fold(_ => List.empty, (_, xs) => xs)

  /** Extract everything except the last element of the NonEmpty list.
    * @return everything except the last element
    */
  def init: List[A] = fold(_ => List.empty, (x, xs) => x :: xs.init)

  /** Produces the first element of the NonEmpty list, and a NonEmpty list of the remaining elements, if any.
    * @return
    */
  def unCons: (A, Maybe[NonEmpty[A]]) = {
    (head, NonEmpty.nonEmpty(tail))
  }

  /** Map a function over a NonEmpty list.
    * @param f the function to be applied
    * @return a new NonEmpty list
    */
  def map[B](f: A => B): NonEmpty[B] = {
    NonEmptyCons(f(head), tail map f)
  }

  /** Map a function over a NonEmpty list.
    * @param f the function to be applied
    * @return a new NonEmpty list
    */
  def flatMap[B](f: A => NonEmpty[B]): NonEmpty[B] = {
    this match {
      case Singleton(x)                   => f(x)
      case NonEmptyCons(x, xs @ (_ :: _)) => f(x) append NonEmpty.fromList(xs).flatMap(f)
      case NonEmptyCons(x, _)             => f(x)
    }
  }

  /** Applies a binary operator to a start value and all elements of this sequence, going left
    * to right.
    * @param z the start value
    * @param f the binary operator
    * @tparam B the result type of the binary operator
    * @return left-associative fold of this list
    */
  def foldLeft[B](z: B)(f: (B, A) => B): B = {
    fold(x => f(z, x), (x, xs) => (x :: xs).foldLeft(z)(f))
  }

  /** Applies a binary operator to a start value and all elements of this sequence, going right to left.
    * @param z the start value
    * @param f the binary operator
    * @tparam B the result type of the binary operator
    * @return right-associative fold of this list
    */
  def foldRight[B](z: B)(f: (A, B) => B): B = {
    fold(x => f(x, z), (x, xs) => (x :: xs).foldRight(z)(f))
  }

  /** Appends this NonEmpty list and that NonEmpty list.
    *
    * @param that the second NonEmpty list
    * @return
    */
  def append(that: NonEmpty[A]): NonEmpty[A] = {
    fold(
      x => NonEmptyCons(x, that.toList),
      (x, xs) => NonEmptyCons(x, xs append that.toList))
  }

  /** Convert a NonEmpty list to a normal list efficiently.
    * @return a normal list
    */
  def toList: List[A] = head :: tail

  override def toString: String = {
    fold(x => s"$x :| []", (x, xs) => s"$x :| [${xs.mkString(",")}]")
  }

  private def fold[B](singletonF: A => B, listF: (A, List[A]) => B): B = {
    this match {
      case Singleton(x)        => singletonF(x)
      case NonEmptyCons(x, xs) => listF(x, xs)
    }
  }
}

object NonEmpty {
  /** nonEmpty efficiently turns a normal list into a NonEmpty list, producing None if the input is empty.
    * @param list
    * @tparam A
    * @return
    */
  def nonEmpty[A](list: List[A]): Maybe[NonEmpty[A]] = {
    list match {
      case x :: xs => Maybe.just(NonEmptyCons(x, xs))
      case _       => Maybe.none
    }
  }

  /** Converts a normal list to a NonEmpty list.
    *
    * Raises an error if given an empty list.
    *
    * @param as list to be converted
    * @return a new NonEmpty list
    */
  def fromList[A](as: List[A]): NonEmpty[A] = {
    nonEmpty(as).getOrElse(error("NonEmpty.fromList: empty list"))
  }

  /** Creates a new NonEmpty list
    *
    * @param x the list head
    * @param xs the list tail
    * @tparam A the element type
    * @return a new NonEmpty list
    */
  def apply[A](x: A, xs: A*): NonEmpty[A] = NonEmptyCons(x, List(xs: _*))

  /** Returns a singleton, non empty list
    *
    * @param x the element
    * @tparam A the element type
    * @return a new NonEmpty list
    */
  def singleton[A](x: A): NonEmpty[A] = Singleton(x)
}

/* Represents a cons non empty list
 */
private[this] case class NonEmptyCons[A](x: A, xs: List[A]) extends NonEmpty[A]

/* Represents the singleton, non empty list
 */
private[this] case class Singleton[A](x: A) extends NonEmpty[A]