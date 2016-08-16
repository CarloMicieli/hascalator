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

package io.hascalator.typeclasses

/**
  * The `Bounded` class is used to name the upper and lower limits of a type. `Ord` is not a superclass of Bounded
  * since types that are not totally ordered may also have upper and lower bounds.
  *
  * @tparam A
  */
trait Bounded[A] extends Any {
  /**
    * Returns the lower limit for the type
    * @return the lower limit
    */
  def minBound: A

  /**
    * Returns the upper limit for the type
    * @return the upper limit
    */
  def maxBound: A
}

object Bounded {
  def apply[A](implicit b: Bounded[A]): Bounded[A] = b

  private def newInstance[A](min: => A)(max: => A): Bounded[A] = new Bounded[A] {
    override def minBound: A = min
    override def maxBound: A = max
  }

  implicit val bool2Bounded: Bounded[Boolean] = newInstance(false)(true)
  implicit val char2Bounded: Bounded[Char] = newInstance(Char.MinValue)(Char.MaxValue)
  implicit val short2Bounded: Bounded[Short] = newInstance(Short.MinValue)(Short.MaxValue)
  implicit val int2Bounded: Bounded[Int] = newInstance(Int.MinValue)(Int.MaxValue)
  implicit val long2Bounded: Bounded[Long] = newInstance(Long.MinValue)(Long.MaxValue)
  implicit val float2Bounded: Bounded[Float] = newInstance(Float.MinValue)(Float.MaxValue)
  implicit val double2Bounded: Bounded[Double] = newInstance(Double.MinValue)(Double.MaxValue)
}