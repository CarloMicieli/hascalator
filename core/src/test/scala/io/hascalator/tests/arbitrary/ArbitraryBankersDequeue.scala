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
package tests
package arbitrary

import Prelude._
import io.hascalator.data.BankersDequeue
import org.scalacheck.{ Arbitrary, Gen }
import org.scalacheck.util.Buildable

import scala.collection.immutable.Traversable
import scala.language.implicitConversions

/** @author Carlo Micieli
  * @since 0.0.1
  */
trait ArbitraryBankersDequeue {
  implicit def bankersDequeueToTraversable[T](q: BankersDequeue[T]): Traversable[T] = new Traversable[T] {
    override def foreach[U](f: (T) => U): Unit = q foreach f
  }

  implicit def buildableBankersDequeue[T]: Buildable[T, BankersDequeue[T]] = new Buildable[T, BankersDequeue[T]] {
    def builder = new scala.collection.mutable.Builder[T, BankersDequeue[T]]() {
      private var q = BankersDequeue.empty[T]

      override def +=(elem: T): this.type = {
        q = q pushBack elem
        this
      }

      override def result(): BankersDequeue[T] = q

      override def clear(): Unit = {
        q = BankersDequeue.empty[T]
      }
    }
  }

  implicit def arbitraryBankersDequeue[T](implicit a: Arbitrary[T]): Arbitrary[BankersDequeue[T]] = Arbitrary {
    import Arbitrary._
    import Gen._

    val genEmptyBankersDequeue = const(BankersDequeue.empty[T])

    def genBankersDequeue = containerOf[BankersDequeue, T](arbitrary[T])

    frequency((1, genEmptyBankersDequeue), (2, genBankersDequeue))
  }
}