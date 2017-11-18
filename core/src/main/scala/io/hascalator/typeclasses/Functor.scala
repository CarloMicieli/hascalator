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

import scala.annotation.implicitNotFound
import scala.language.higherKinds

/** A `Functor` is a type constructor `F` that assigns a type `F[A]` to each type `A`, together with a polymorphic
  * functional [[Functor#map]] that lifts a function `g : A => B` to a function `map g : F[A] => F[B]`.
  *
  * Functors uniform action over a parameterized type, generalizing the `map` function on lists.
  *
  * Instances of Functor should satisfy the following laws:
  *
  * `fmap id  ==  id`
  * `fmap (f . g)  ==  fmap f . fmap g`
  *
  * @tparam F
  * @author Carlo Micieli
  * @since 0.0.1
  */
@implicitNotFound("The type ${F} was not made instance of the Functor type class")
trait Functor[F[_]] extends Any { self =>
  /** Applies the function `f` to this `Functor` instance
    * @param fa a `Functor`
    * @param f the function to apply
    * @tparam A
    * @tparam B
    * @return
    */
  def map[A, B](fa: F[A])(f: A => B): F[B]

  /** Lifts a function `f` in a `Functor` context.
    * @param f
    * @tparam A
    * @tparam B
    * @return
    */
  def lift[A, B](f: A => B): F[A] => F[B] = map(_)(f)

  trait FunctorLaws {
    def identity[A](fa: F[A])(implicit E: Eq[F[A]]): Boolean = E.eq(fa, map(fa)(id))

    def composite[A, B, C](fa: F[A], f1: A => B, f2: B => C)(implicit E: Eq[F[C]]): Boolean = {
      E.eq(map(map(fa)(f1))(f2), map(fa)(f1 andThen f2))
    }
  }

  def laws: FunctorLaws = new FunctorLaws {}
}

object Functor {
  def apply[F[_], A](implicit fa: Functor[F]): Functor[F] = fa

  trait FunctorOps[F[_], A] {
    def functorInstance: Functor[F]
    def self: F[A]
    def map[B](f: A => B): F[B] = functorInstance.map(self)(f)
  }

  object ops {
    implicit class FunctorSyntax[F[_], A](fa: F[A])(implicit ev: Functor[F]) extends FunctorOps[F, A] {
      override def functorInstance: Functor[F] = ev
      override def self: F[A] = fa
    }
  }

  implicit def eitherInstance[C]: Functor[Either[C, ?]] = new Functor[Either[C, ?]] {
    override def map[A, B](fa: Either[C, A])(f: (A) => B): Either[C, B] = fa map f
  }

  implicit val listInstance: Functor[List] = new Functor[List] {
    override def map[A, B](fa: List[A])(f: (A) => B): List[B] = fa map f
  }

  implicit val maybeInstance: Functor[Maybe] = new Functor[Maybe] {
    override def map[A, B](fa: Maybe[A])(f: (A) => B): Maybe[B] = fa map f
  }

  implicit val nonEmptyInstance: Functor[NonEmpty] = new Functor[NonEmpty] {
    override def map[A, B](fa: NonEmpty[A])(f: (A) => B): NonEmpty[B] = fa map f
  }
}
