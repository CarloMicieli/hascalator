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
package control

import Prelude._
import scala.annotation.implicitNotFound

/** @tparam F
  * @author Carlo Micieli
  * @since 0.0.1
  */
@implicitNotFound("The type ${F} was not made instance of the Applicative type class")
trait Applicative[F[_]] extends Functor[F] { self =>
  /** Lifts a value
    * @param x
    * @tparam A
    * @return
    */
  def pure[A](x: A): F[A]

  /** Sequential application.
    *
    * @param x
    * @param f
    * @tparam A
    * @tparam B
    * @return
    */
  def <*>[A, B](x: F[A])(f: F[A => B]): F[B]

  def *>[A, B](fa: F[A])(fb: F[B]): F[B] = fb

  def <*[A, B](fa: F[A])(fb: F[B]): F[A] = fa

  trait ApplicativeLaws {
    def identity[A](v: A): Boolean = {
      false
    }
  }
}

object Applicative {
  def apply[F[_]](implicit ev: Applicative[F]): Applicative[F] = ev
}

