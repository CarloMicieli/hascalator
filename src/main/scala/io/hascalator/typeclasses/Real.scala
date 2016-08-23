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
import io.hascalator.math.Rational
import scala.language.implicitConversions

/**
  *
  * @tparam A the instance type
  * @author Carlo Micieli
  * @since 0.0.1
  */
trait Real[A] extends Any with Num[A] with Ord[A] {
  /**
    * Return the rational equivalent of its real argument with full precision
    * @param x
    * @return
    */
  def toRational(x: A): Rational
}

object Real {
  def apply[A](implicit r: Real[A]): Real[A] = r

  trait RealOps[A] {
    def self: A
    def realInstance: Real[A]

    def toRational: Rational = realInstance.toRational(self)

    object ops {
      implicit def toRealOps(x: A)(implicit r: Real[A]): RealOps[A] = new RealOps[A] {
        override def self: A = x
        override def realInstance: Real[A] = r
      }
    }
  }
}