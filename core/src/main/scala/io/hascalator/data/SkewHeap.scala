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
package data

import Prelude._

import scala.util.control.NoStackTrace

/** A ''skew heap'' (a representation of priority queues) based upon Chris Okasaki's implementation.
  * Insert operation based upon the John Hughes's implementation.
  *
  * Skew  heaps  are  binary  trees,  but  unlike  many heap implementations, they don’t have
  * any other constraints. Binary heaps, for instance,  are  generally  required  to  be  completely
  * balanced. The structure of a skew heap is just a vanilla binary tree.
  *
  * @tparam A the element data type
  * @author Carlo Micieli
  * @since 0.0.1
  */
private[data] sealed trait SkewHeap[+A] extends MergeableHeap[A] {
  override type MH[B] = SkewHeap[B]

  override def min: Maybe[A] = {
    this match {
      case Fork(x, _, _) => Maybe.just(x)
      case _             => Maybe.none
    }
  }

  override def extractMin[A1 >: A](implicit ord: Ord[A1]): Either[EmptyHeapException, (A1, SkewHeap[A1])] = {
    this match {
      case EmptyHeap => Either.left(new EmptyHeapException with NoStackTrace)
      case Fork(v, l, r) =>
        val res = (v, l.merge[A1](r))
        Either.right(res)
    }
  }

  override def merge[A1 >: A](that: MH[A1])(implicit ord: Ord[A1]): MH[A1] = {
    (this, that) match {
      case (l, EmptyHeap) => l
      case (EmptyHeap, r) => r
      case (l, r) =>
        if (ord.lte(l.min.get, r.min.get)) {
          l join r
        } else {
          r join l
        }
    }
  }

  override def insert[A1 >: A](x: A1)(implicit ord: Ord[A1]): SkewHeap[A1] = {
    this match {
      case EmptyHeap => Fork(x, SkewHeap.empty[A1], SkewHeap.empty[A1])
      case Fork(y, l, r) =>
        val (min, max) = minMax(x, y)
        Fork(min, r, l.insert(max))
    }
  }

  override def size: Int = {
    this match {
      case EmptyHeap     => 0
      case Fork(_, l, r) => 1 + l.size + r.size
    }
  }

  private def balanced: Boolean = {
    this match {
      case EmptyHeap => true
      case Fork(_, l, r) =>
        val d = r.size - l.size
        (d == 0 || d == 1) && l.balanced && r.balanced
    }
  }

  private def minMax[A1 >: A](x: A1, y: A1)(implicit ord: Ord[A1]): (A1, A1) = {
    import Ord.ops._
    if (x < y) {
      (x, y)
    } else {
      (y, x)
    }
  }

  private def invariant[A1 >: A](implicit ord: Ord[A1]): Boolean = {
    this match {
      case EmptyHeap     => true
      case Fork(x, l, r) => smaller[A1](x, l) && smaller[A1](x, r)
    }
  }

  private def smaller[A1 >: A](x: A1, t: SkewHeap[A1])(implicit ord: Ord[A1]): Boolean = {
    t match {
      case EmptyHeap => true
      case Fork(y, _, _) =>
        import Ord.ops._
        x <= y && t.invariant
    }
  }

  private def credits: Int = {
    this match {
      case EmptyHeap => 0
      case h @ Fork(_, l, r) =>
        l.credits + r.credits + (if (h.isGood) 0 else 1)
    }
  }

  private[SkewHeap] def isGood: Boolean = {
    this match {
      case Fork(_, l, r) => l.size <= r.size
      case _             => error("SkewHeap.isGood: tree is empty")
    }
  }

  private def join[A1 >: A](that: MH[A1])(implicit ord: Ord[A1]): MH[A1] = {
    (this, that) match {
      case (Fork(x, l, r), h) =>
        val r2 = l.merge[A1](h)(ord)
        Fork[A1](x, r, l merge h)
      case _ =>
        error("SkewHeap.join: tree is empty")
    }
  }
}

object SkewHeap {
  def empty[A: Ord]: SkewHeap[A] = EmptyHeap

  def fromList[A: Ord](xs: List[A]): SkewHeap[A] = {
    xs.foldLeft(SkewHeap.empty[A])((tree, x) => tree insert x)
  }
}

private[data] case class Fork[A](get: A, left: SkewHeap[A], right: SkewHeap[A]) extends SkewHeap[A] {
  def isEmpty: Boolean = false
}

private[data] case object EmptyHeap extends SkewHeap[Nothing] {
  def isEmpty: Boolean = true
}