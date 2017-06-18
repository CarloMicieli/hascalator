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
package dst

import Prelude._

import scala.util.control.NoStackTrace

/** A Leftist tree heap.
  *
  * @author Carlo Micieli
  * @since 0.0.1
  */
sealed trait Leftist[+A] {

  /** In Leftist tree, a rank value is defined for each node. Rank is the distance
    * to the nearest external node.
    *
    * @return this node rank
    */
  def rank: Int

  /** Returns the number of elements contained in this Leftist tree.
    * @return the size
    */
  def size: Int = this match {
    case LEmpty            => 0
    case LNode(_, _, l, r) => 1 + l.size + r.size
  }

  /** Insert a new key.
    *
    * @usecase def insert(k: A): Leftist[A]
    * @param k the new key
    * @param ord
    * @tparam A1
    * @return a new Leftist tree with the new key
    */
  def insert[A1 >: A](k: A1)(implicit ord: Ord[A1]): Leftist[A1] = {
    val r = Leftist.singleton(k)
    this.merge[A1](r)(ord)
  }

  /** Find and remove the minimum element in this Leftist tree.
    *
    * Because this operation must preserve the __heap property__ is bound to O(lg n).
    *
    * @usecase def pop: Either[EmptyLeftistException, (A, Leftist[A])]
    * @tparam A1 the key type
    * @return the minimum key (if any)
    */
  def pop[A1 >: A](implicit ord: Ord[A1]): Either[EmptyLeftistException, (A1, Leftist[A1])] = {
    this match {
      case LEmpty => Either.left(new EmptyLeftistException with NoStackTrace)
      case LNode(_, k, l, r) =>
        val res = (k, l.merge[A1](r)(ord))
        Either.right(res)
    }
  }

  /** Finds the minimum element in this Leftist tree.
    *
    * @return the minimum key
    */
  def top: Maybe[A] = {
    this match {
      case LEmpty            => Maybe.none
      case LNode(_, k, _, _) => Maybe.just(k)
    }
  }

  /** Returns true when this Leftist tree is empty, false otherwise
    * @return true if empty
    */
  def isEmpty: Boolean

  /** Merge two Leftist trees, preserving the heap property.
    *
    * @usecase def merge(that: Leftist[A]): Leftist[A]
    * @param that
    * @param ord
    * @tparam A1
    * @return
    */
  def merge[A1 >: A](that: Leftist[A1])(implicit ord: Ord[A1]): Leftist[A1] = {
    (this, that) match {
      case (LEmpty, _) => that
      case (_, LEmpty) => this
      case (LNode(_, k1, l1, r1), LNode(_, k2, l2, r2)) =>
        if (ord.lt(k1, k2)) {
          Leftist.mk(k1, l1, r1 merge that)
        } else {
          Leftist.mk(k2, l2, this merge r2)
        }
    }
  }
}

object Leftist extends LeftistInstances {
  /** A new empty Leftist tree
    * @tparam A the key type
    * @return a new empty tree
    */
  def empty[A: Ord]: Leftist[A] = LEmpty

  /** A new singleton Leftist tree
    * @param x the key
    * @tparam A the key type
    * @return a Leftist tree
    */
  def singleton[A: Ord](x: A): Leftist[A] = LNode(1, x, LEmpty, LEmpty)

  /** Build the Leftist heap from a list
    * @param xs a List
    * @tparam A the key type
    * @return a Leftist tree
    */
  def fromList[A: Ord](xs: List[A]): Leftist[A] = {
    xs.foldLeft(empty[A])((tree, x) => tree.insert(x))
  }

  private def mk[A](k: A, la: Leftist[A], lb: Leftist[A]): Leftist[A] = {
    if (la.rank < lb.rank) {
      LNode(la.rank + 1, k, lb, la)
    } else {
      LNode(lb.rank + 1, k, la, lb)
    }
  }
}

private[dst] case object LEmpty extends Leftist[Nothing] {
  override def rank: Int = 0

  override def isEmpty: Boolean = true
}

private[dst] final case class LNode[A](rank: Int, element: A, left: Leftist[A], right: Leftist[A])
    extends Leftist[A] {
  override def isEmpty: Boolean = false
}

trait LeftistInstances {
  implicit class eqInstance[A: Eq](t: Leftist[A]) extends Eq[Leftist[A]] {
    override def eq(lhs: Leftist[A], rhs: Leftist[A]): Boolean = lhs.equals(rhs)
  }
}

class EmptyLeftistException extends Exception("This Leftist tree is empty")