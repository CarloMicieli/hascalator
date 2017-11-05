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
package dst

import Prelude._

/** Splay tree provides a light weight implementation with acceptable dynamic balancing result.
  *
  * @author Carlo Micieli
  * @since 0.0.1
  */
sealed trait SplayTree[+A] {

  /** Returns true when this Splay tree is empty, false otherwise.
    * @return true when this is empty; false otherwise
    */
  def isEmpty: Boolean

  /** Returns the number of keys stored in this Splay tree.
    * @return the number of keys
    */
  def size: Int = {
    this match {
      case EmptySplay     => 0
      case SNode(l, _, r) => 1 + l.size + r.size
    }
  }

  /** @return
    */
  @tailrec final def top: Maybe[A] = {
    this match {
      case EmptySplay              => Maybe.none
      case SNode(EmptySplay, k, _) => Maybe.just(k)
      case SNode(l, _, _)          => l.top
    }
  }

  /** @return
    */
  def pop: (A, SplayTree[A]) = {
    this match {
      case EmptySplay                             => error("empty")
      case SNode(EmptySplay, min, r)              => (min, r)
      case SNode(SNode(EmptySplay, x1, r1), x, r) => (x1, SNode(r1, x, r))
      case SNode(SNode(l1, x1, r1), x, r) =>
        val (min, tree) = l1.pop
        (min, SNode(tree, x1, SNode(r1, x, r)))
    }
  }

  /** Merge two Splay trees.
    *
    * @usecase def merge(that: SplayTree[A]): SplayTree[A]
    * @param that
    * @param ord
    * @tparam A1
    * @return
    */
  def merge[A1 >: A](that: SplayTree[A1])(implicit ord: Ord[A1]): SplayTree[A1] = {
    this match {
      case EmptySplay => that
      case SNode(l, x, r) =>
        val (l1, r1) = that partition x
        SNode(l merge l1, x, r merge r1)
    }
  }

  /** Inserts a new key in this Splay tree.
    *
    * @usecase def insert(key: A): SplayTree[A]
    * @param key the new key
    * @param ord
    * @tparam A1
    * @return a new Splay tree
    */
  def insert[A1 >: A](key: A1)(implicit ord: Ord[A1]): SplayTree[A1] = {
    this match {
      case EmptySplay => SNode(EmptySplay, key, EmptySplay)
      case SNode(l, x, r) =>
        if (ord.gt(x, key)) {
          SNode(l.insert(key), x, r) splay key
        } else {
          SNode(l, x, r.insert(key)) splay key
        }
    }
  }

  protected final def partition[A1 >: A](pivot: A1)(implicit ord: Ord[A1]): (SplayTree[A1], SplayTree[A1]) = {
    if (isEmpty) {
      (EmptySplay, EmptySplay)
    } else {
      val SNode(l, x, r) = this
      if (ord.lt(x, pivot)) {
        r match {
          case EmptySplay => (this, EmptySplay)
          case SNode(l1, x1, r1) =>
            if (ord.lt(x1, pivot)) {
              val (small, big) = r1 partition pivot
              (SNode(SNode(l, x, l1), x1, small), big)
            } else {
              val (small, big) = l1 partition pivot
              (SNode(l, x, small), SNode(big, x1, r1))
            }
        }
      } else {
        l match {
          case EmptySplay => (EmptySplay, this)
          case SNode(l1, x1, r1) =>
            if (ord.lt(pivot, x1)) {
              val (small, _) = l1 partition pivot
              (small, SNode(l1, x1, SNode(r1, x, r)))
            } else {
              val (small, big) = r1 partition pivot
              (SNode(l1, x1, small), SNode(big, x, r))
            }
        }
      }

    }
  }

  protected final def splay[A1 >: A](key: A1)(implicit ord: Ord[A1]): SplayTree[A1] = {
    (this, key) match {
      // zig-zig
      case (t @ SNode(SNode(SNode(a, x, b), p, c), g, d), y) =>
        if (ord.eq(x, y)) {
          SNode(a, x, SNode(b, p, SNode(c, g, d)))
        } else {
          t
        }
      case (t @ SNode(a, g, SNode(b, p, SNode(c, x, d))), y) =>
        if (ord.eq(x, y)) {
          SNode(SNode(SNode(a, g, b), p, c), x, d)
        } else {
          t
        }
      // zig-zag
      case (t @ SNode(SNode(a, p, SNode(b, x, c)), g, d), y) =>
        if (ord.eq(x, y)) {
          SNode(SNode(a, p, b), x, SNode(c, g, d))
        } else {
          t
        }
      case (t @ SNode(a, g, SNode(SNode(b, x, c), p, d)), y) =>
        if (ord.eq(x, y)) {
          SNode(SNode(a, g, b), x, SNode(c, p, d))
        } else {
          t
        }
      // zig
      case (t @ SNode(SNode(a, x, b), p, c), y) =>
        if (ord.eq(x, y)) {
          SNode(a, x, SNode(b, p, c))
        } else {
          t
        }
      case (t @ SNode(a, p, SNode(b, x, c)), y) =>
        if (ord.eq(x, y)) {
          SNode(SNode(a, p, b), x, c)
        } else {
          t
        }
      case (t, _) => t
    }
  }
}

private[dst] case object EmptySplay extends SplayTree[Nothing] {
  override def isEmpty: Boolean = true
}

private[dst] case class SNode[A](left: SplayTree[A], key: A, right: SplayTree[A]) extends SplayTree[A] {
  override def isEmpty: Boolean = false
}

object SplayTree {
  /** Creates a new empty SplayTree.
    * @tparam A the key type
    * @return a new empty SplayTree
    */
  def empty[A: Ord]: SplayTree[A] = EmptySplay

  def fromList[A: Ord](xs: List[A]): SplayTree[A] = {
    xs.foldLeft(empty[A])((tree, x) => tree insert x)
  }
}

