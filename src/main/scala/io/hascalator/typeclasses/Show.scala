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
import scala.StringContext

import Prelude._

/**
  * It represents the type class for conversion of values to readable Strings.
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

  def apply[A](toStr: A => String): Show[A] = new Show[A] {
    override def show(x: A): String = toStr(x)
  }

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

  implicit val booleanShow: Show[Boolean] = apply(_.toString)
  implicit val byteShow: Show[Byte] = apply(_.toString)
  implicit val intShow: Show[Int] = apply(_.toString)
  implicit val shortShow: Show[Short] = apply(_.toString)
  implicit val longShow: Show[Long] = apply(_.toString)
  implicit val floatShow: Show[Float] = apply(_.toString)
  implicit val doubleShow: Show[Double] = apply(_.toString)
  implicit val stringShow: Show[String] = apply(x => s""""$x"""")

  implicit val charShow: Show[Char] = new Show[Char] {
    override def show(x: Char): String = s"'$x'"
    override def showList(xs: List[Char]): String = xs.mkString("", "\"", "\"")
  }

}