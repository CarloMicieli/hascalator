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

/** @author Carlo Micieli
  * @since 0.0.1
  */
private[data] sealed trait PairingHeap[+A] extends MergeableHeap[A] {
  override type MH[B] = PairingHeap[B]

  override def merge[A1 >: A](that: PairingHeap[A1])(implicit ord: Ord[A1]): PairingHeap[A1] = {
    (this, that) match {
      case (PairNode(x1, ts1), PairNode(x2, ts2)) =>
        if (ord.lte(x1, x2)) {
          PairNode(x1, that :: ts1)
        } else {
          PairNode(x2, this :: ts2)
        }
      case (EmptyPH, _) => that
      case (_, EmptyPH) => this
    }
  }

  override def insert[A1 >: A](k: A1)(implicit ord: Ord[A1]): PairingHeap[A1] = {
    this merge PairNode(k, List.empty[PairingHeap[A1]])
  }

  override def size: Int = {
    this match {
      case EmptyPH => 0
      case PairNode(_, ts) =>
        ts.foldLeft(1)((n, ph) => n + ph.size)
    }
  }

  override def min: Maybe[A] = {
    this match {
      case EmptyPH        => Maybe.none
      case PairNode(x, _) => Maybe.just(x)
    }
  }

  override def extractMin[A1 >: A](implicit ord: Ord[A1]): Either[EmptyHeapException, (A1, Heap[A1])] = {
    def meldChildren(ps: List[PairingHeap[A1]]): PairingHeap[A1] = {
      ps match {
        case t1 :: t2 :: ts => (t1 merge t2) merge meldChildren(ts)
        case List(t)        => t
        case List()         => PairingHeap.empty[A1]
      }
    }

    this match {
      case EmptyPH         => Either.left(new EmptyHeapException with NoStackTrace)
      case PairNode(x, ts) => Either.right((x, meldChildren(ts)))
    }
  }
}

private[data] object EmptyPH extends PairingHeap[Nothing] {
  override def isEmpty: Boolean = true
}

private[data] case class PairNode[A](x: A, ps: List[PairingHeap[A]]) extends PairingHeap[A] {
  override def isEmpty: Boolean = false
}

/** @author Carlo Micieli
  * @since 0.0.1
  */
object PairingHeap {

  def empty[A: Ord]: PairingHeap[A] = EmptyPH

  def fromList[A: Ord](xs: List[A]): PairingHeap[A] = {
    xs.foldLeft(empty[A])((h, x) => h insert x)
  }
}
