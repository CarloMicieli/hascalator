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

package io.hascalator.dst

import io.hascalator.Ord
import io.hascalator.functions._
import io.hascalator.data.{ List, Maybe }
import Maybe._

private[this] sealed trait RBTree[+K, +V] extends Any with Tree[K, V] {
  override def lookup[K1 >: K](key: K1)(implicit ord: Ord[K1]): Maybe[V] = {
    this match {
      case EmptyRBTree                              => none
      case RBNode(_, left, k, v, right) if k == key => just(v)
      case RBNode(_, left, k, _, right) =>
        import Ord.ops._
        if (key < k) {
          left.lookup(key)
        } else {
          right.lookup(key)
        }
    }
  }

  override def insert[K1 >: K, V1 >: V](key: K1, value: V1)(implicit ord: Ord[K1]): Tree[K1, V1] = {
    def makeBlack(t: RBTree[K1, V1]): RBTree[K1, V1] = t match {
      case RBNode(_, l, k, v, r) => RBNode(Black, l, k, v, r)
      case _                     => throw new Exception("invalid value " + t)
    }

    def ins(t: RBTree[K1, V1]): RBTree[K1, V1] = {
      t match {
        case EmptyRBTree => new RBNode(key, value)
        case RBNode(color, l, k, v, r) =>
          import Ord.ops._
          if (key < k) {
            RBTree.balance(color, ins(l), k, v, r)
          } else {
            RBTree.balance(color, l, k, v, ins(r))
          }
      }
    }

    makeBlack(ins(this))
  }

  override def max: Maybe[K] = {
    this match {
      case EmptyRBTree                     => none
      case RBNode(_, _, k, _, EmptyRBTree) => just(k)
      case RBNode(_, _, _, _, right)       => right.max
    }
  }

  override def min: Maybe[K] = {
    this match {
      case EmptyRBTree                     => none
      case RBNode(_, EmptyRBTree, k, _, _) => just(k)
      case RBNode(_, left, _, _, _)        => left.min
    }
  }

  override def size: Int = this match {
    case EmptyRBTree                  => 0
    case RBNode(_, left, _, _, right) => 1 + left.size + right.size
  }

  override def toList: List[(K, V)] = this match {
    case EmptyRBTree                  => List.empty[(K, V)]
    case RBNode(_, left, k, v, right) => left.toList ++ List((k, v)) ++ right.toList
  }

  override def fold[V1 >: V](f: (V1, V1) => V1): V1 = {
    this match {
      case EmptyRBTree                               => throw new NoSuchElementException("fold: tree is empty")
      case RBNode(_, EmptyRBTree, _, v, EmptyRBTree) => v
      case RBNode(_, left, _, v, EmptyRBTree)        => f(left.fold(f), v)
      case RBNode(_, EmptyRBTree, _, v, right)       => f(v, right.fold(f))
      case RBNode(_, left, _, v, right)              => f(v, f(left.fold(f), right.fold(f)))
    }
  }

  override def delete[K1 >: K](key: K1)(implicit ord: Ord[K1]): (Maybe[V], Tree[K1, V]) = ???

  override def contains[K1 >: K](key: K1)(implicit ord: Ord[K1]): Boolean = {
    this match {
      case EmptyRBTree                       => false
      case RBNode(_, _, k, _, _) if k == key => true
      case RBNode(_, left, k, _, right) =>
        import Ord.ops._
        if (key < k) {
          left.contains(key)
        } else {
          right.contains(key)
        }
    }
  }

  override def depth: Int = {
    this match {
      case EmptyRBTree => 0
      case RBNode(_, left, _, _, right) =>
        1 + math.max(left.depth, right.depth)
    }
  }

  override def upsert[K1 >: K, V1 >: V](key: K1, value: V1)(f: (V1) => V1)(implicit ord: Ord[K1]): Tree[K1, V1] = ???

  override def map[V1](f: (V) => V1): Tree[K, V1] = ???

  override def toString: String = {
    this match {
      case EmptyRBTree => "-"
      case RBNode(_, l, k, v, r) =>
        s"($l [$k->$v] $r)"
    }
  }
}

private case object EmptyRBTree extends RBTree[Nothing, Nothing] {
  override def get: Nothing = error("Tree.get: tree is empty")
  override def isEmpty: Boolean = true
}

private case class RBNode[K, V](color: Color, left: RBTree[K, V], key: K, value: V, right: RBTree[K, V]) extends RBTree[K, V] {

  def this(key: K, value: V) = this(Red, EmptyRBTree, key, value, EmptyRBTree)

  override def isEmpty: Boolean = false
  override def get: (K, V) = (key, value)
}

/**
  * Red-black tree is a type of self-balancing binary search tree. By using color
  * changing and rotation, red-black tree provides a very simple and straightforward
  * way to keep the tree balanced.
  *
  * A binary search tree red-black tree satisfies the following 5 properties:
  *  - Every node is either red or black.
  *  - The root is black.
  *  - Every leaf (`NIL`) is black.
  *  - If a node is red, then both its children are black.
  *  - For each node, all paths from the node to descendant leaves contain the same number of black nodes.
  */
object RBTree {

  /**
    * It creates a new empty red-black tree.
    * @tparam K the key type
    * @tparam V the value type
    * @return an empty tree
    */
  def empty[K, V](implicit ord: Ord[K]): Tree[K, V] = EmptyRBTree

  /**
    * It creates a new red-black tree, initialized with the provided list elements.
    * @param xs the initial elements of the tree
    * @tparam K the key type
    * @tparam V the value type
    * @return a tree
    */
  def fromList[K, V](xs: List[(K, V)])(implicit ord: Ord[K]): Tree[K, V] = {
    xs.foldLeft(RBTree.empty[K, V])((tree, x) => tree insert x)
  }

  private def balance[K, V](color: Color, left: RBTree[K, V], key: K, value: V, right: RBTree[K, V])(implicit ord: Ord[K]): RBTree[K, V] = {
    (color, left, key, value, right) match {
      case (Black, RBNode(Red, RBNode(Red, a, kx, vx, b), ky, vy, c), kz, vz, d) =>
        RBNode(Red, RBNode(Black, a, kx, vx, b), ky, vy, RBNode(Black, c, kz, vz, d))
      case (Black, RBNode(Red, a, kx, vx, RBNode(Red, b, ky, vy, c)), kz, vz, d) =>
        RBNode(Red, RBNode(Black, a, kx, vx, b), ky, vy, RBNode(Black, c, kz, vz, d))
      case (Black, a, kx, vx, RBNode(Red, b, ky, vy, RBNode(Red, c, kz, vz, d))) =>
        RBNode(Red, RBNode(Black, a, kx, vx, b), ky, vy, RBNode(Black, c, kz, vz, d))
      case (Black, a, kx, vx, RBNode(Red, RBNode(Red, b, ky, vy, c), kz, vz, d)) =>
        RBNode(Red, RBNode(Black, a, kx, vx, b), ky, vy, RBNode(Black, c, kz, vz, d))
      case (c, l, k, v, r) => RBNode(color, l, k, v, r)
    }
  }
}

private[this] sealed trait Color
private[this] case object Red extends Color
private[this] case object Black extends Color
