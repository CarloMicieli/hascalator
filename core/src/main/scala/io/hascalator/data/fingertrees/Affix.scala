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
package fingertrees

import Prelude._

/** Data structure to hold the finger tree prefix/suffix (between 1 and 4 elements).
  * @tparam A the element type
  * @author Carlo Micieli
  * @since 0.1
  */
private[fingertrees] sealed trait Affix[+A] extends Any {
  def last: A
  def init: Affix[A]

  def toList: List[A] = {
    this match {
      case One(x)           => List(x)
      case Two(x, y)        => List(x, y)
      case Three(x, y, w)   => List(x, y, w)
      case Four(x, y, w, z) => List(x, y, w, z)
    }
  }

  def prepend[A1 >: A](a: A1): Affix[A1] = {
    this match {
      case One(x)         => Two(a, x)
      case Two(x, y)      => Three(a, x, y)
      case Three(x, y, w) => Four(a, x, y, w)
      case _              => error("Affix must have one to four elements")
    }
  }

  def append[A1 >: A](a: A1): Affix[A1] = {
    this match {
      case One(x)         => Two(x, a)
      case Two(x, y)      => Three(x, y, a)
      case Three(x, y, w) => Four(x, y, w, a)
      case _              => error("Affix must have one to four elements")
    }
  }
}

private[fingertrees] object Affix {

  implicit def toMeasured[V, A](implicit mva: Measured[V, A]): Measured[V, Affix[A]] = new Measured[V, Affix[A]] {
    override def measure(affix: Affix[A]): V = {
      mva.mConcat(affix.toList)
    }
    override def mempty: V = mva.mempty
    override def mappend(x: V, y: V): V = mva.mappend(x, y)
  }

  def apply[A](x: A): Affix[A] = One(x)
  def apply[A](x: A, y: A): Affix[A] = Two(x, y)
  def apply[A](x: A, y: A, w: A): Affix[A] = Three(x, y, w)
  def apply[A](x: A, y: A, w: A, z: A): Affix[A] = Four(x, y, w, z)

  def fromList[A](xs: List[A]): Affix[A] = {
    xs match {
      case List(x)          => One(x)
      case List(x, y)       => Two(x, y)
      case List(x, y, w)    => Three(x, y, w)
      case List(x, y, w, z) => Four(x, y, w, z)
      case _                => error("Affix must have one to four elements")
    }
  }
}

private[fingertrees] final case class One[A](x: A) extends Affix[A] {
  override def init: Affix[A] = error("Affix must have one to four elements")
  override def last: A = x
}

private[fingertrees] final case class Two[A](x: A, y: A) extends Affix[A] {
  override def init: Affix[A] = One(x)
  override def last: A = y
}

private[fingertrees] final case class Three[A](x: A, y: A, w: A) extends Affix[A] {
  override def init: Affix[A] = Two(x, y)
  override def last: A = w
}

private[fingertrees] final case class Four[A](x: A, y: A, w: A, z: A) extends Affix[A] {
  override def init: Affix[A] = Three(x, y, w)
  override def last: A = z
}
