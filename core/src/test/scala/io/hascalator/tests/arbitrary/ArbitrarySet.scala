/*
 * Copyright 2016 CarloMicieli
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
package tests.arbitrary

import Prelude._
import org.scalacheck.{ Arbitrary, Gen }
import org.scalacheck.util.Buildable

import scala.collection.mutable
import scala.language.implicitConversions

/** @author Carlo Micieli
  * @since 0.0.1
  */
trait ArbitrarySet {

  implicit def setToTraversable[T](set: Set[T]): Traversable[T] = new Traversable[T] {
    override def foreach[U](f: (T) => U): Unit = set.foreach(f)
  }

  implicit def arbitrarySet[T](implicit a: Arbitrary[T]): Arbitrary[Set[T]] = Arbitrary {
    import Arbitrary._
    import Gen._

    val genEmptySet = const(Set.empty[T])

    val genSingletonSet = for { x <- arbitrary[T] } yield Set.singleton(x)

    def genSet(sz: Int): Gen[Set[T]] = containerOfN[Set, T](sz, arbitrary[T])

    def sizedSet(sz: Int) = {
      if (sz <= 0) { genEmptySet }
      else { Gen.frequency((1, genEmptySet), (1, genSingletonSet), (8, genSet(sz))) }
    }

    Gen.sized(sz => sizedSet(sz))
  }

  implicit def setBuildable[A]: Buildable[A, Set[A]] = new Buildable[A, Set[A]] {
    override def builder = new mutable.Builder[A, Set[A]] {
      private var set = Set.empty[A]

      override def +=(elem: A): this.type = {
        set = set.insert(elem)
        this
      }

      override def result(): Set[A] = set

      override def clear(): Unit = set = Set.empty[A]
    }
  }
}
