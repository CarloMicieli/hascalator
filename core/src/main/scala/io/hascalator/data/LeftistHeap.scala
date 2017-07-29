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
import scala.util.control.NoStackTrace

/** A Leftist tree heap.
  *
  * @tparam A the heap item type
  * @author Carlo Micieli
  * @since 0.0.1
  */
private[data] sealed trait LeftistHeap[+A] extends MergeableHeap[A] {

  override type MH[B] = LeftistHeap[B]

  override def size: Int = this match {
    case LEmpty            => 0
    case LNode(_, _, l, r) => 1 + l.size + r.size
  }

  override def min: Maybe[A] = {
    this match {
      case LEmpty            => Maybe.none
      case LNode(_, k, _, _) => Maybe.just(k)
    }
  }

  override def extractMin[A1 >: A](implicit ord: Ord[A1]): Either[EmptyHeapException, (A1, LeftistHeap[A1])] = {
    this match {
      case LEmpty => Either.left(new EmptyHeapException with NoStackTrace)
      case LNode(_, k, l, r) =>
        val res = (k, l.merge[A1](r)(ord))
        Either.right(res)
    }
  }

  override def insert[A1 >: A](k: A1)(implicit ord: Ord[A1]): LeftistHeap[A1] = {
    val r = LeftistHeap.singleton(k)
    this merge r
  }

  override def merge[A1 >: A](that: LeftistHeap[A1])(implicit ord: Ord[A1]): LeftistHeap[A1] = {
    (this, that) match {
      case (LEmpty, _) => that
      case (_, LEmpty) => this
      case (LNode(_, k1, l1, r1), LNode(_, k2, l2, r2)) =>
        if (ord.lt(k1, k2)) {
          val nr = r1.merge[A1](that)(ord)
          LeftistHeap.mk(k1, l1, nr)
        } else {
          val nr = this.merge[A1](r2)(ord)
          LeftistHeap.mk(k2, l2, nr)
        }
    }
  }

  /** In Leftist tree, a rank value is defined for each node. Rank is the distance
    * to the nearest external node.
    *
    * @return this node rank
    */
  def rank: Int
}

private[data] case object LEmpty extends LeftistHeap[Nothing] {
  override def rank: Int = 0

  override def isEmpty: Boolean = true
}

private[data] final case class LNode[A](rank: Int, element: A, left: LeftistHeap[A], right: LeftistHeap[A])
    extends LeftistHeap[A] {
  override def isEmpty: Boolean = false
}

object LeftistHeap {
  /** A new empty Leftist tree
    * @tparam A the key type
    * @return a new empty tree
    */
  def empty[A: Ord]: LeftistHeap[A] = LEmpty

  /** A new singleton Leftist tree
    * @param x the key
    * @tparam A the key type
    * @return a Leftist tree
    */
  def singleton[A: Ord](x: A): LeftistHeap[A] = LNode(1, x, LEmpty, LEmpty)

  /** Build the Leftist heap from a list
    * @param xs a List
    * @tparam A the key type
    * @return a Leftist tree
    */
  def fromList[A: Ord](xs: List[A]): LeftistHeap[A] = {
    xs.foldLeft(empty[A])((tree, x) => tree.insert(x))
  }

  private def mk[A](k: A, la: LeftistHeap[A], lb: LeftistHeap[A]): LeftistHeap[A] = {
    if (la.rank < lb.rank) {
      LNode(la.rank + 1, k, lb, la)
    } else {
      LNode(lb.rank + 1, k, la, lb)
    }
  }
}