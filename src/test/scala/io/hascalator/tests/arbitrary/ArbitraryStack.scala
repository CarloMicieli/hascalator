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
package tests.arbitrary

import Prelude._
import io.hascalator.data.Stack
import org.scalacheck.{ Arbitrary, Gen }
import org.scalacheck.util.Buildable

import scala.collection.immutable.Traversable
import scala.language.implicitConversions

/**
  * @author Carlo Micieli
  * @since 0.0.1
  */
trait ArbitraryStack {
  implicit def stackToTraversable[T](stack: Stack[T]): Traversable[T] = new Traversable[T] {
    override def foreach[U](f: (T) => U): Unit = stack.foreach(f)
  }

  implicit def buildableStack[T]: Buildable[T, Stack[T]] = new Buildable[T, Stack[T]] {
    def builder = new scala.collection.mutable.Builder[T, Stack[T]]() {
      private var stack = Stack.empty[T]

      override def +=(elem: T): this.type = {
        stack = stack push elem
        this
      }

      override def result(): Stack[T] = stack

      override def clear(): Unit = stack = Stack.empty[T]
    }
  }

  implicit def arbitraryStack[T](implicit a: Arbitrary[T]): Arbitrary[Stack[T]] = Arbitrary {
    import Arbitrary._
    import Gen._

    val genEmptyStack = const(Stack.empty[T])

    def genStack = containerOf[Stack, T](arbitrary[T])

    frequency((1, genEmptyStack), (2, genStack))
  }
}
