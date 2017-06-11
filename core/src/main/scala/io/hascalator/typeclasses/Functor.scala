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
import data.NonEmpty

import scala.language.higherKinds

/** Functors: uniform action over a parameterized type, generalizing the map function on lists.
  *
  * @tparam F
  * @author Carlo Micieli
  * @since 0.0.1
  */
trait Functor[F[_]] extends Any { self =>
  /** @param f
    * @param fa
    * @tparam A
    * @tparam B
    * @return
    */
  def map[A, B](f: A => B)(fa: F[A]): F[B]
}

object Functor {
  def apply[F[_], A](implicit fa: Functor[F]): Functor[F] = fa

  trait FunctorOps[F[_], A] {
    def functorInstance: Functor[F]
    def self: F[A]
    def map[B](f: A => B): F[B] = functorInstance.map(f)(self)
  }

  object ops {
    implicit def toFunctorOp[F[_], A](fa: F[A])(implicit ev: Functor[F]) = new FunctorOps[F, A] {
      override def self: F[A] = fa
      override def functorInstance: Functor[F] = ev
    }
  }

  implicit val listInstance: Functor[List] = new Functor[List] {
    override def map[A, B](f: (A) => B)(fa: List[A]): List[B] = fa map f
  }

  implicit val maybeInstance: Functor[Maybe] = new Functor[Maybe] {
    override def map[A, B](f: (A) => B)(fa: Maybe[A]): Maybe[B] = fa map f
  }

  implicit val nonEmptyInstance: Functor[NonEmpty] = new Functor[NonEmpty] {
    override def map[A, B](f: (A) => B)(fa: NonEmpty[A]): NonEmpty[B] = fa map f
  }
}