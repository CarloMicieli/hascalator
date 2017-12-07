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

import scala.inline
import Prelude._
import Ordering._

/** An efficient implementation of sets.
  *
  * The implementation of Set is based on size balanced binary trees (or trees
  * of bounded balance) as described by:
  *
  * - Adams, Stephen. "Implementing Sets Efficiently in a Functional Language."
  * - Haskell Prelude `Data.Set`
  *
  * @tparam A the element type
  * @author Carlo Micieli
  * @since 0.0.1
  */
sealed trait Set[+A] {
  /** `O(1)`. Is this the empty set?
    * @return `true` when this `Set` is empty; `false` otherwise
    */
  def isEmpty: Boolean

  /** `O(1)`. The number of elements in the set.
    * @return the number of elements
    */
  def size: Int

  /** `O(log n)`. Is the element in the set?
    * @param x the element to find
    * @return `true` if `x` is a set memeber; `false` otherwise
    * @usecase def member(x: A): Boolean
    */
  def member[B >: A](x: B)(implicit ord: Ord[B]): Boolean = {
    this match {
      case EmptySet => false
      case TreeS(v, _, l, r) =>
        if (ord.eq(x, v)) {
          true
        } else if (ord.lt(x, v)) {
          l.member(x)
        } else {
          r.member(x)
        }
    }
  }

  /** `O(log n)`. Is the element not in the set?
    * @param x the element to find
    * @return `true` if `x` is not a set memeber; `false` otherwise
    * @usecase def notMember(x: A): Boolean
    */
  @inline def notMember[B >: A](x: B)(implicit ord: Ord[B]): Boolean = {
    !this.member(x)
  }

  /** `O(log n)` - for debugging
    */
  def foreach[U](f: A => U): Unit = {
    this match {
      case EmptySet => ()
      case TreeS(v, _, l, r) =>
        f(v)
        l.foreach(f)
        r.foreach(f)
        ()
    }
  }

  /** `O(log n)`. Insert an element in a set. If the set already contains an
    * element equal to the given value, it is replaced with the new value.
    *
    * @param x the element to add
    * @return a new set, with the newly added element
    * @usecase def insert[A](x: A): Set[A]
    */
  def insert[B >: A](x: B)(implicit ord: Ord[B]): Set[B] = {
    this match {
      case EmptySet => new TreeS(x)
      case TreeS(v, _, l, r) =>
        if (ord.lt(x, v)) {
          Set.T(v, l.insert(x), r)
        } else if (ord.lt(v, x)) {
          Set.T(v, l, r.insert(x))
        } else {
          this
        }
    }
  }

  /** `O(log n)`. Delete an element from a set.
    *
    * @param x the element to remove
    * @return a new set, with the element `x` removed from the set
    * @usecase def delete(x: A): Set[A]
    */
  def delete[B >: A](x: B)(implicit ord: Ord[B]): Set[B] = {
    this match {
      case EmptySet => EmptySet
      case TreeS(v, _, l, r) =>
        if (ord.lt(x, v)) {
          Set.T(v, l.delete(x), r)
        } else if (ord.lt(v, x)) {
          Set.T(v, l, r.delete(x))
        } else {
          Set.delete(l, r)
        }
    }
  }

  /** `O(log n)`. Delete the minimal element. Returns an empty set if the set is empty.
    * @return a new set after the minimum element has been deleted
    */
  def deleteMin: Set[A] = {
    this match {
      case EmptySet                 => EmptySet
      case TreeS(_, _, EmptySet, r) => r
      case TreeS(v, _, l, r)        => Set.T(v, l.deleteMin, r)
    }
  }

  /** `O(n*log n)`. Returns the set obtained by applying `f` to each element of `this` set.
    *
    * It's worth noting that the size of the result may be smaller if, for
    * some (x,y), x /= y && f x == f y
    */
  def map[B](f: A => B)(implicit ord: Ord[B]): Set[B] = {
    Set.fromList(toAscList.map(f))
  }

  /** `O(n)`. Fold the elements in the set using the given left-associative binary operator, such that foldl f z == foldl f z . toAscList.
    */
  def foldLeft[B](seed: B)(f: (B, A) => B): B = {
    def fold(z: B, s: Set[A]): B = {
      s match {
        case EmptySet          => z
        case TreeS(x, _, l, r) => fold(f(fold(z, l), x), r)
      }
    }

    fold(seed, this)
  }

  /** `O(n)`. Fold the elements in the set using the given right-associative binary operator, such that foldr f z == foldr f z . toAscList.
    */
  def foldRight[B](seed: B)(f: (A, B) => B): B = {
    def fold(acc: B, set: Set[A]): B = {
      //HACK: no pattern matching here, to avoid a SO on equals()
      if (set.isEmpty) {
        acc
      } else {
        val TreeS(v, _, l, r) = set
        fold(f(v, fold(acc, r)), l)
      }
    }

    fold(seed, this)
  }

  /** `O(m*log(n/m + 1))`, `m <= n`. The union of two sets, preferring the first
    * set when equal elements are encountered.
    *
    * @param that another set
    * @return the union of `this` and `that`
    * @usecase def union(that: Set[A]): Set[A]
    */
  def union[B >: A](that: Set[B])(implicit ord: Ord[B]): Set[B] = {
    (this, that) match {
      case (EmptySet, s2) => s2
      case (s1, EmptySet) => s1
      case (s1, TreeS(v, _, l, r)) =>
        val l1 = Set.splitLt(s1, v)
        val r1 = Set.splitGt(s1, v)
        Set.concat3(v, l1.union(l), r1.union(r))
    }
  }

  /** `O(m*log(n/m + 1))`, `m <= n`. The intersection of two sets.
    *
    * @param that another set
    * @return the intersection of `this` and `that`
    * @usecase def intersection(that: Set[A]): Set[A]
    */
  def intersection[B >: A](that: Set[B])(implicit ord: Ord[B]): Set[B] = {
    (this, that) match {
      case (EmptySet, _) => EmptySet
      case (_, EmptySet) => EmptySet
      case (s, TreeS(v, _, l, r)) =>
        val l1 = Set.splitLt(s, v)
        val r1 = Set.splitGt(s, v)

        if (s.member(v)) {
          Set.concat3(v, l1.intersection(l), r1.intersection(r))
        } else {
          Set.concat(l1.intersection(l), r1.intersection(r))
        }
    }
  }

  /** `O(m*log(n/m + 1))`, `m <= n`. Difference of two sets.
    *
    * @param that another set
    * @return the difference of `this` and `that`
    * @usecase def difference(that: Set[A]): Set[A]
    */
  def difference[B >: A](that: Set[B])(implicit ord: Ord[B]): Set[B] = {
    (this, that) match {
      case (s, EmptySet) => s
      case (s, TreeS(v, _, l, r)) =>
        val l1 = Set.splitLt(s, v)
        val r1 = Set.splitGt(s, v)
        Set.concat(l1.difference(l), r1.difference(r))
    }
  }

  /** `O(log n)`. Find largest element smaller than the given one.
    *
    * {{{
    * scala> Set.fromList(List(3, 5)).lookupLT(3)
    * res0: Maybe[Int] = none
    *
    * scala> Set.fromList(List(3, 5)).lookupLT(5)
    * res1: Maybe[Int] = Just 5
    * }}}
    *
    * @usecase def lookupLT(x: A): Maybe[A]
    */
  def lookupLT[B >: A](x: B)(implicit ord: Ord[B]): Maybe[B] = {
    @tailrec def findLT(best: B, set: Set[B]): Maybe[B] = {
      set match {
        case EmptySet => Maybe.just(best)
        case TreeS(y, _, l, r) =>
          if (ord.lte(x, y)) {
            findLT(best, l)
          } else {
            findLT(y, r)
          }
      }
    }

    this match {
      case EmptySet => Maybe.none
      case TreeS(y, _, l, _) =>
        if (ord.lte(x, y)) {
          l.lookupLT(x)
        } else {
          findLT(y, l)
        }
    }
  }

  /** `O(log n)`. Find largest element smaller than the given one.
    *
    * {{{
    * scala> Set.fromList(List(3, 5)).lookupGT(4)
    * res0: Maybe[Int] = Just 5
    *
    * scala> Set.fromList(List(3, 5)).lookupGT(5)
    * res1: Maybe[Int] = none
    * }}}
    *
    * @usecase def lookupGT(x: A): Maybe[A]
    */
  def lookupGT[B >: A](x: B)(implicit ord: Ord[B]): Maybe[B] = {
    def findGT(best: B, set: Set[A]): Maybe[B] = {
      set match {
        case EmptySet => Maybe.just(best)
        case TreeS(y, _, l, r) =>
          if (ord.lt(x, y)) {
            findGT(y, l)
          } else {
            findGT(best, r)
          }
      }
    }

    this match {
      case EmptySet => Maybe.none
      case TreeS(y, _, l, r) =>
        if (ord.lt(x, y)) {
          findGT(y, l)
        } else {
          r.lookupGT(x)
        }
    }
  }

  /** `O(log n)`. Find largest element smaller or equal to the given one.
    *
    * {{{
    * scala> Set.fromList(List(3, 5)).lookupLE(2)
    * res0: Maybe[Int] = none
    *
    * scala> Set.fromList(List(3, 5)).lookupLE(4)
    * res1: Maybe[Int] = Just 3
    *
    * scala> Set.fromList(List(3, 5)).lookupLE(5)
    * res2: Maybe[Int] = Just 5
    * }}}
    */
  def lookupLE[B >: A](x: B)(implicit ord: Ord[B]): Maybe[B] = {
    def findLE(best: B, set: Set[A]): Maybe[B] = {
      set match {
        case EmptySet => Maybe.just(best)
        case TreeS(y, _, l, r) =>
          ord.compare(x, y) match {
            case LT => findLE(best, l)
            case EQ => Maybe.just(y)
            case GT => findLE(y, r)
          }
      }
    }

    this match {
      case EmptySet => Maybe.none
      case TreeS(y, _, l, r) =>
        ord.compare(x, y) match {
          case LT => l.lookupLE(x)
          case EQ => Maybe.just(y)
          case GT => findLE(y, r)
        }
    }
  }

  /** `O(log n)`. Find largest element smaller or equal to the given one.
    *
    * {{{
    * scala> Set.fromList(List(3, 5)).lookupGE(3)
    * res0: Maybe[Int] = Just 3
    *
    * scala> Set.fromList(List(3, 5)).lookupGE(4)
    * res1: Maybe[Int] = Just 5
    *
    * scala> Set.fromList(List(3, 5)).lookupGE(6)
    * res2: Maybe[Int] = none
    * }}}
    */
  def lookupGE[B >: A](x: B)(implicit ord: Ord[B]): Maybe[B] = {
    def findGE(best: B, set: Set[A]): Maybe[B] = {
      set match {
        case EmptySet => Maybe.just(best)
        case TreeS(y, _, l, r) =>
          ord.compare(x, y) match {
            case LT => findGE(y, l)
            case EQ => Maybe.just(y)
            case GT => findGE(best, r)
          }
      }
    }

    this match {
      case EmptySet => Maybe.none
      case TreeS(y, _, l, r) =>
        ord.compare(x, y) match {
          case LT => findGE(y, l)
          case EQ => Maybe.just(y)
          case GT => r.lookupGE(x)
        }
    }
  }

  /** `O(log n)`. The minimal element of a set.
    */
  def lookupMin: Maybe[A] = {
    this match {
      case EmptySet                 => Maybe.none
      case TreeS(v, _, EmptySet, _) => Maybe.just(v)
      case TreeS(_, _, left, _)     => left.lookupMin
    }
  }

  /** `O(log n)`. The maximal element of a set.
    */
  def lookupMax: Maybe[A] = {
    this match {
      case EmptySet                 => Maybe.none
      case TreeS(v, _, _, EmptySet) => Maybe.just(v)
      case TreeS(_, _, _, right)    => right.lookupMax
    }
  }

  /** `O(log n)`. The minimal element of a set.
    */
  def findMin: A = {
    lookupMin.getOrElse(error("Set.findMin: empty set has no minimal element"))
  }

  /** `O(log n)`. The maximal element of a set.
    */
  def findMax: A = {
    lookupMax.getOrElse(error("Set.findMax: empty set has no maximal element"))
  }

  /** `O(n+m)`. Is this a subset? `s1.isSubsetOf(s2)` tells whether `s1` is a subset of `s2`.
    */
  def isSubsetOf[B >: A](that: Set[B])(implicit ord: Ord[B]): Boolean = {
    if (this.size > that.size) {
      false
    } else {
      Set.isSubsetOf(this, that)
    }
  }

  /** `O(n+m)`. Is this a proper subset? (ie. a subset but not equal).
    */
  def isProperSubsetOf[B >: A](that: Set[B])(implicit ord: Ord[B]): Boolean = {
    (this.size < that.size) && this.isSubsetOf(that)
  }

  /** `O(1)`. Decompose a set into pieces based on the structure of the underlying tree.
    *
    * {{{
    * scala> Set.fromList(List.fromRange(1 to 6)).splitRoot
    * res0: List[Set[Int]] = [fromList [1, 2, 3], fromList [4], fromList [5, 6]]
    *
    * scala> Set.empty[Int].splitRoot
    * res1: List[Set[Int]] = []
    * }}}
    */
  def splitRoot: List[Set[A]] = {
    this match {
      case EmptySet             => List.empty
      case TreeS(root, _, l, r) => List(l, Set.singleton(root), r)
    }
  }

  /** `O(log n)`. Performs a 'split' but also returns whether the pivot element was found in the original set.
    *
    * @usecase def splitMember(x: A)(implicit ord: Ord[A]): (Set[A], Boolean, Set[A])
    */
  def splitMember[B >: A](x: B)(implicit ord: Ord[B]): (Set[B], Boolean, Set[B]) = {
    this match {
      case EmptySet => (EmptySet, false, EmptySet)
      case TreeS(y, _, l, r) =>
        ord.compare(x, y) match {
          case LT =>
            val (lt, found, gt) = l.splitMember(x)
            (lt, found, Set.concat3(y, gt, r))
          case GT =>
            val (lt, found, gt) = r.splitMember(x)
            (Set.concat3(y, l, lt), found, gt)
          case EQ => (l, true, r)
        }
    }
  }

  /** `O(log n)`.
    */
  def rank[B >: A](x: B)(implicit ord: Ord[B]): Int = {
    this match {
      case EmptySet => error("Set.rank: subscript")
      case TreeS(v, _, l, r) =>
        if (ord.lt(x, v)) {
          l.rank(x)
        } else if (ord.gt(x, v)) {
          r.rank(x) + l.size + 1
        } else {
          l.size
        }
    }
  }

  /** `O(log n)`.
    */
  def index(i: Int): A = {
    this match {
      case EmptySet => error("Set.index: subscript")
      case TreeS(v, _, l, r) =>
        val nl = l.size
        if (i < nl) {
          l.index(i)
        } else if (i > nl) {
          r.index(i - nl - 1)
        } else {
          v
        }
    }
  }

  /** `O(n)`. Convert the set to an ascending list of elements. Subject to list fusion.
    */
  def toAscList: List[A] = {
    foldRight(List.empty[A])((x, xs) => x :: xs)
  }

  /** `O(n)`. Convert the set to a descending list of elements. Subject to list fusion.
    */
  def toDescList: List[A] = {
    foldLeft(List.empty[A])((xs, x) => x :: xs)
  }

  override def equals(obj: scala.Any): Boolean = {
    obj match {
      case that: Set[_] => this.size == that.size && this.toAscList == that.toAscList
      case _            => false
    }
  }

  override def toString: String = {
    val elements = toAscList.toString
    s"fromList $elements"
  }
}

private[data] case object EmptySet extends Set[Nothing] {
  override def isEmpty: Boolean = true
  override def size: Int = 0
}

private[data] final case class TreeS[A](element: A, count: Int, left: Set[A], right: Set[A]) extends Set[A] {
  def this(v: A) = {
    this(v, 1, EmptySet, EmptySet)
  }

  override def isEmpty: Boolean = false
  override def size: Int = count
}

object Set extends SetInstances {
  /** `O(1)`. The empty set.
    */
  def empty[A]: Set[A] = EmptySet

  /** `O(1)`. Create a singleton set.
    */
  def singleton[A](x: A): Set[A] = new TreeS(x)

  /** `O(n*log n)`. Create a set from a list of elements.
    */
  def fromList[A: Ord](as: List[A]): Set[A] = {
    as.foldLeft(Set.empty[A])((set, x) => set.insert(x))
  }

  /** The union of a list of sets:
    */
  def unions[A: Ord](ssa: List[Set[A]]): Set[A] = {
    ssa.foldLeft(Set.empty[A])((set, set1) => set1 union set)
  }

  // Auxiliary function to build a tree containing all the elements of `left` and `right`.
  def delete[A](left: Set[A], right: Set[A]): Set[A] = {
    (left, right) match {
      case (EmptySet, r) => r
      case (l, EmptySet) => l
      case (l, r) =>
        val minEl = r.findMin
        T(minEl, l, r.deleteMin)
    }
  }

  private def isSubsetOf[A](set1: Set[A], set2: Set[A])(implicit ord: Ord[A]): Boolean = {
    (set1, set2) match {
      case (EmptySet, _) => true
      case (_, EmptySet) => false
      case (TreeS(x, _, l, r), t) =>
        val (lt, found, gt) = t.splitMember(x)(ord)
        found && isSubsetOf(l, lt) && isSubsetOf(r, gt)
    }
  }

  // Single left-rotation
  private def singleL[A](a: A, left: Set[A], right: Set[A]): Set[A] = {
    (left, right) match {
      case (x, TreeS(b, _, y, z)) => N(b, N(a, x, y), z)
      case _                      => error("singleL: right is empty")
    }
  }

  // Double left-rotation
  private def doubleL[A](a: A, left: Set[A], right: Set[A]): Set[A] = {
    (left, right) match {
      case (x, TreeS(c, _, TreeS(b, _, y1, y2), z)) => N(b, N(a, x, y1), N(c, y2, z))
      case _                                        => error("doubleL: right is empty")
    }
  }

  // Single right-rotation
  private def singleR[A](a: A, left: Set[A], right: Set[A]): Set[A] = {
    (left, right) match {
      case (TreeS(b, _, x, y), z) => N(b, x, N(a, y, z))
      case _                      => error("singleR: left is empty")
    }
  }

  // Double left-rotation
  private def doubleR[A](c: A, left: Set[A], right: Set[A]): Set[A] = {
    (left, right) match {
      case (TreeS(a, _, x, TreeS(b, _, y1, y2)), z) => N(b, N(a, x, y1), N(c, y2, z))
      case _                                        => error("doubleR: left is empty")
    }
  }

  // This is the smart constructor that ensures that the size of the tree is maintained correctly.
  // The tree (v,l,r) must already be balanced.
  private def N[A](el: A, left: Set[A], right: Set[A]): Set[A] = {
    TreeS(el, 1 + left.size + right.size, left, right)
  }

  // O(log n)
  private def splitLt[A](s: Set[A], x: A)(implicit ord: Ord[A]): Set[A] = {
    s match {
      case EmptySet => EmptySet
      case TreeS(v, _, l, r) =>
        if (ord.lt(x, v)) {
          splitLt(l, x)
        } else if (ord.lt(v, x)) {
          concat3(v, l, splitLt(r, x))
        } else {
          l
        }
    }
  }

  // O(log n)
  private def splitGt[A](s: Set[A], x: A)(implicit ord: Ord[A]): Set[A] = {
    s match {
      case EmptySet => EmptySet
      case TreeS(v, _, l, r) =>
        if (ord.gt(x, v)) {
          splitGt(r, x)
        } else if (ord.gt(v, x)) {
          concat3(v, splitGt(l, x), r)
        } else {
          r
        }
    }
  }

  private def concat[A: Ord](set1: Set[A], set2: Set[A]): Set[A] = {
    (set1, set2) match {
      case (s1, EmptySet) => s1
      case (s1, s2)       => Set.concat3(s2.findMin, s1, s2.deleteMin)
    }
  }

  // O(log n - log m) joins two sets using an element v.
  private def concat3[A: Ord](v: A, s1: Set[A], s2: Set[A]): Set[A] = {
    (s1, s2) match {
      case (EmptySet, r) => r.insert(v)
      case (l, EmptySet) => l.insert(v)
      case (TreeS(v1, n1, l1, r1), TreeS(v2, n2, l2, r2)) =>
        if (n1 * weight < n2) {
          T(v2, concat3(v, s1, l2), r2)
        } else if (n2 * weight < n1) {
          T(v1, l1, concat3(v, r1, s2))
        } else {
          N(v, s1, s2)
        }
    }
  }

  // is the maximal relative difference between the sizes of two trees, it corresponds with the [w] in Adams' paper.
  private val weight: Int = 3

  private def T[A](v: A, l: Set[A], r: Set[A]): Set[A] = {
    val ln = l.size
    val rn = r.size

    if (ln + rn < 2) {
      N(v, l, r)
    } else if (rn > ln * weight) { //right is too big
      val TreeS(_, _, rl, rr) = r
      val rln = rl.size
      val rrn = rr.size

      if (rln < rrn) {
        singleL(v, l, r)
      } else {
        doubleL(v, l, r)
      }
    } else if (ln > rn * weight) { //left is too big
      val TreeS(_, _, ll, lr) = l
      val lln = ll.size
      val lrn = lr.size

      if (lrn < lln) {
        singleR(v, l, r)
      } else {
        doubleR(v, l, r)
      }
    } else { // neither tree is too heavy
      N(v, l, r)
    }
  }
}

trait SetInstances {
  implicit def toEq[A: Eq]: Eq[Set[A]] = (lhs: Set[A], rhs: Set[A]) => {
    val eqList: Eq[List[A]] = implicitly[Eq[List[A]]]
    lhs.size == rhs.size && eqList.eq(lhs.toAscList, rhs.toAscList)
  }

  implicit def toShowSet[A: Show]: Show[Set[A]] = (x: Set[A]) => {
    val elements: String = implicitly[Show[A]].showList(x.toAscList)
    s"fromList $elements"
  }
}