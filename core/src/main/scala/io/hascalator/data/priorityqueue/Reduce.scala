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
package priorityqueue

import Prelude._
import scala.language.higherKinds

/** The reduction typeclass: a __reduction__ is a function that collapses a structure of type `F[A]` into a single value
  * of type `A`.
  *
  * @tparam F
  */
trait Reduce[F[_], A] {
  /** Collapses a structure of type `F[A]` into a single value `A` from right to left
    * @param f
    * @tparam B
    * @return
    */
  def reduceRight[B](f: (A, B) => B): (F[A], B) => B

  /** Collapses a structure of type `F[A]` into a single value `A` from left to right
    * @param f
    * @tparam B
    * @return
    */
  def reduceLeft[B](f: (B, A) => B): (B, F[A]) => B
}

object Reduce {
  def apply[F[_], A](implicit ev: Reduce[F, A]): Reduce[F, A] = ev

  def toList[F[_], A](s: F[A])(implicit R: Reduce[F, A]): List[A] = {
    R.reduceRight[List[A]](_ :: _)(s, List.empty)
  }

  implicit def listInstance[A]: Reduce[List, A] = new Reduce[List, A] {
    override def reduceLeft[B](f: (B, A) => B): (B, List[A]) => B = {
      (z, as) => as.foldLeft(z)(f)
    }
    override def reduceRight[B](f: (A, B) => B): (List[A], B) => B = {
      (as, z) => as.foldRight(z)(f)
    }
  }
}