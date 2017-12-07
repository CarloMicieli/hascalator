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
package typeclasses

import Prelude._
import io.hascalator.data.NonEmpty

import scala.annotation.implicitNotFound
import scala.language.implicitConversions

/** A __semigroup__ is an algebraic structure consisting of a set together with an associative binary operation.
  *
  * @tparam A the instance type
  * @author Carlo Micieli
  * @since 0.0.1
  */
@implicitNotFound("The type ${A} was not made instance of the Semigroup type class")
trait Semigroup[A] extends Any {
  /** An associative operation.
    * @param x the first operand
    * @param y the second operand
    * @return the result applying the operation to x and y
    */
  def mappend(x: A, y: A): A

  /** Reduce a non-empty list with [[mappend]].
    *
    * @param xs a non empty list
    * @return the result
    */
  def sConcat(xs: NonEmpty[A]): A = {
    xs.tail.foldLeft(xs.head)(mappend)
  }

  /** Repeat a value n times.
    *
    * Given that this works on a Semigroup it is allowed to fail if you request 0 or fewer repetitions, and the
    * default definition will do so.
    *
    * @param times
    * @param z
    * @return
    */
  def sTimes(times: Int)(z: A): A = {
    @tailrec def go(n: Int, acc: A): A = {
      if (n <= 1) {
        acc
      } else {
        go(n - 1, mappend(acc, z))
      }
    }

    if (times <= 0) {
      error("Semigroup.sTimes: invalid value")
    }

    go(times, z)
  }
}

object Semigroup {
  def apply[A](implicit sg: Semigroup[A]): Semigroup[A] = sg

  trait SemigroupOps[A] {
    def self: A
    def semigroupInstance: Semigroup[A]

    def <>(that: A): A = semigroupInstance.mappend(self, that)
  }

  object ops {
    implicit def toSemigroupOps[A: Semigroup](x: A): SemigroupOps[A] = new SemigroupOps[A] {
      override def self: A = x
      override def semigroupInstance: Semigroup[A] = implicitly[Semigroup[A]]
    }
  }

  implicit def listSemigroup[A]: Semigroup[List[A]] = new Semigroup[List[A]] {
    override def mappend(x: List[A], y: List[A]): List[A] = x append y
  }
}