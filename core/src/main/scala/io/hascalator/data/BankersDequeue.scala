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

import Prelude._

/** An implementation of Banker's Dequeues, as described in Chris Okasaki's
  * Purely Functional Data Structures.
  *
  * @param sizeF the front list length
  * @param front the front list
  * @param sizeR the rear list length
  * @param rear the rear list
  * @tparam A the deque type
  * @author Carlo Micieli
  * @since 0.0.1
  */
final case class BankersDequeue[A] private (
    private val sizeF: Int,
    private val front: List[A],
    private val sizeR: Int,
    private val rear: List[A]) extends Dequeue[A] {

  def foreach[U](f: A => U): Unit = {
    front.foreach(f)
    rear.reverse.foreach(f)
  }

  override def isEmpty: Boolean = {
    length == 0
  }

  override def length: Int = {
    sizeR + sizeF
  }

  override def first: Maybe[A] = {
    this match {
      case BankersDequeue(_, List(), _, List(x)) => Maybe.just(x)
      case BankersDequeue(_, fs, _, _)           => fs.headMaybe
    }
  }

  override def last: Maybe[A] = {
    this match {
      case BankersDequeue(_, List(x), _, List()) => Maybe.just(x)
      case BankersDequeue(_, _, _, rs)           => rs.headMaybe
    }
  }

  override def reverse: Dequeue[A] = {
    BankersDequeue(sizeR, rear, sizeF, front)
  }

  override def pushFront(x: A): BankersDequeue[A] = {
    import BankersDequeue._
    check(BankersDequeue(sizeF + 1, x :: front, sizeR, rear))
  }

  override def popFront: Maybe[(A, BankersDequeue[A])] = {
    import BankersDequeue._
    this match {
      case BankersDequeue(_, List(), _, List())  => Maybe.none
      case BankersDequeue(_, List(), _, List(x)) => Maybe.just((x, empty))
      case BankersDequeue(_, List(), _, _)       => throw new UnbalancedDequeueException
      case BankersDequeue(sf, (f :: fs), sr, rs) =>
        val res = (f, check(BankersDequeue(sf - 1, fs, sr, rs)))
        Maybe.just(res)
    }
  }

  override def pushBack(x: A): BankersDequeue[A] = {
    import BankersDequeue._
    check(BankersDequeue(sizeF, front, sizeR + 1, x :: rear))
  }

  override def popBack: Maybe[(A, BankersDequeue[A])] = {
    import BankersDequeue._
    this match {
      case BankersDequeue(_, List(), _, List())  => Maybe.none
      case BankersDequeue(_, List(x), _, List()) => Maybe.just((x, empty))
      case BankersDequeue(_, _, _, List())       => throw new UnbalancedDequeueException
      case BankersDequeue(sf, fs, sr, (r :: rs)) =>
        val res = (r, check(BankersDequeue(sf, fs, sr - 1, rs)))
        Maybe.just(res)
    }
  }

  override def takeFront(n: Int): List[A] = {
    (front take n) ++ rear.reverse.take(n - sizeF)
  }

  override def takeBack(n: Int): List[A] = {
    (rear take n) ++ front.reverse.take(n - sizeR)
  }

  override def toList: List[A] = {
    front ++ rear.reverse
  }
}

/** An implementation of Banker's Dequeues, as described in Chris Okasaki's
  * Purely Functional Data Structures.
  *
  * The functions for the Dequeue instance have the following complexities
  * (where `n` is the length of the queue):
  *
  * - `length`: O(1)
  * - `first`: O(1)
  * - `last`: O(1)
  * - `takeFront`: O(n)
  * - `takeBack`: O(n)
  * - `pushFront`: O(1) amortised
  * - `popFront`: O(1) amortised
  * - `pushBack`: O(1) amortised
  * - `popBack`: O(1) amortised
  * - `fromList`: O(n)
  *
  * @author Carlo Micieli
  * @since 0.0.1
  */
object BankersDequeue {
  // The maximum number of times longer one half of a 'BankersDequeue' is
  // permitted to be relative to the other.
  private val bqBalance: Int = 4

  def empty[A]: BankersDequeue[A] = BankersDequeue(0, List.empty[A], 0, List.empty[A])

  // Converts a list into a dequeue.
  def fromList[A](as: List[A]): BankersDequeue[A] = {
    check(BankersDequeue(as.length, as, 0, List.empty[A]))
  }

  // Checks to see if the queue is too far out of balance. If it is, it
  // rebalances it.
  private def check[A](q: BankersDequeue[A]): BankersDequeue[A] = {
    val BankersDequeue(sizeF, front, sizeR, rear) = q
    val size1 = (sizeF + sizeR) / 2
    val size2 = (sizeF + sizeR) - size1

    if (sizeF > bqBalance * sizeR + 1) {
      val frontP = front take size1
      val rearP = rear ++ (front drop size1).reverse
      BankersDequeue(size1, frontP, size2, rearP)
    } else if (sizeR > bqBalance * sizeF + 1) {
      val frontP = front ++ (rear drop size1).reverse
      val rearP = rear take size1
      BankersDequeue(size2, frontP, size1, rearP)
    } else {
      q
    }
  }
}

final class UnbalancedDequeueException extends java.lang.Exception("Dequeue is too far unbalanced.")
