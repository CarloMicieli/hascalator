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

import scala.language.implicitConversions
import scala.annotation.implicitNotFound

import Prelude._

/** It represents the type class for conversion of values to readable Strings.
  * @tparam A the instance type
  * @author Carlo Micieli
  * @since 0.0.1
  */
@implicitNotFound("Type ${A} was not made an instance of the Show type class")
trait Show[A] extends Any {
  def show(x: A): String
  def showList(xs: List[A]): String = xs.map(show).mkString(", ", "[", "]")
}

object Show {
  def apply[A](implicit S: Show[A]): Show[A] = S

  def fromFunction[A](toStr: A => String): Show[A] = (x: A) => toStr(x)

  trait ShowOps[A] {
    def self: A
    def showInstance: Show[A]
    def show: String = showInstance.show(self)
  }

  object ops {
    implicit def toShowOps[A: Show](x: A): ShowOps[A] = new ShowOps[A] {
      override def self: A = x
      override def showInstance: Show[A] = implicitly[Show[A]]
    }
  }

  implicit val booleanShow: Show[Boolean] = fromFunction((x: Boolean) => x.toString)
  implicit val byteShow: Show[Byte] = fromFunction((x: Byte) => x.toString)
  implicit val intShow: Show[Int] = fromFunction((x: Int) => x.toString)
  implicit val shortShow: Show[Short] = fromFunction((x: Short) => x.toString)
  implicit val longShow: Show[Long] = fromFunction((x: Long) => x.toString)
  implicit val floatShow: Show[Float] = fromFunction((x: Float) => x.toString)
  implicit val doubleShow: Show[Double] = fromFunction((x: Double) => x.toString)
  implicit val stringShow: Show[String] = fromFunction((x: String) => s""""$x"""")

  implicit val charShow: Show[Char] = new Show[Char] {
    override def show(x: Char): String = s"'$x'"
    override def showList(xs: List[Char]): String = xs.mkString("", "\"", "\"")
  }

}