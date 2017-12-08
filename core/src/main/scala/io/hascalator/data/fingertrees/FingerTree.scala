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
package fingertrees

import Prelude._

import scala.inline

/** A representation of a sequence of values of type a, allowing access to the ends in constant time, and append
  * and split in time logarithmic in the size of the smaller piece.
  *
  * The collection is also parameterized by a measure type v, which is used to specify a position in the sequence
  * for the split operation. The types of the operations enforce the constraint `Measured[V, A]`, which also implies
  * that the type `V` is determined by `A`.
  *
  * A variety of abstract data types can be implemented by using different element types and measurements.
  *
  * @author Carlo Micieli
  * @since 0.1
  */
sealed trait FingerTree[+V, +A] extends Any {

  /** `O(1) amortized` Prepend the element `x` to the finger tree
    *
    * @param x
    * @tparam A1
    * @return
    */
  def prepend[V1 >: V, A1 >: A](x: A1)(implicit mva: Measured[V1, A1]): FingerTree[V1, A1] = {
    this match {
      case Empty     => Single(x)
      case Single(y) => FingerTree.deep(Affix(x).toList, Empty, Affix(y).toList)
      case Deep(_, Four(a, b, c, d), deeper, suffix) =>
        val node = Node[V1, A1](b, c, d)
        val deeper1 = deeper.prepend[V1, Node[V1, A1]](node)
        FingerTree.deep(Affix(x, a).toList, deeper1, suffix.toList)
      case tree @ Deep(_, _, _, _) =>
        tree.copy(prefix = tree.prefix.prepend(x))
    }
  }

  @inline def <|[V1 >: V, A1 >: A](x: A1)(implicit mva: Measured[V1, A1]): FingerTree[V1, A1] = {
    this.prepend[V1, A1](x)(mva)
  }

  /** `O(1) amortized` Append the element `y` to the finger tree
    *
    * @param y
    * @tparam A1
    * @return
    */
  def append[V1 >: V, A1 >: A](y: A1)(implicit mva: Measured[V1, A1]): FingerTree[V1, A1] = {
    this match {
      case Empty     => Single(y)
      case Single(x) => FingerTree.deep(Affix(x).toList, Empty, Affix(y).toList)
      case Deep(_, prefix, deeper, Four(a, b, c, d)) =>
        val node = Node[V1, A1](a, b, c)
        val deeper1 = deeper.append[V1, Node[V1, A1]](node)
        FingerTree.deep(prefix.toList, deeper1, Two(d, y).toList)
      case tree @ Deep(_, _, _, _) =>
        tree.copy(suffix = tree.suffix.append(y))
    }
  }

  @inline def |>[V1 >: V, A1 >: A](y: A1)(implicit mva: Measured[V1, A1]): FingerTree[V1, A1] = {
    this.append[V1, A1](y)(mva)
  }

  def viewRight[V1 >: V, A1 >: A](implicit mva: Measured[V1, A1]): View[V1, A1] = {
    val mvaa: Measured[V1, Affix[A1]] = Measured[V1, Affix[A1]]
    val mvat: Measured[V1, FingerTree[V1, Node[V1, A1]]] = Measured[V1, FingerTree[V1, Node[V1, A1]]]

    def computeTreeRemainder(prefix: Affix[A1], deeper: FingerTree[V1, Node[V1, A1]]) = {
      deeper.viewRight match {
        case TView(node, rest) =>
          val suff = Affix.fromList(node.toList)
          val annot = mva.mAppend3(mvaa.measure(prefix), mvat.measure(rest), mvaa.measure(suff))
          Deep(annot, prefix, rest, suff)
        case Nil => FingerTree.affixToTree(prefix)
      }
    }

    this match {
      case Empty     => Nil
      case Single(x) => TView(x, Empty)
      case Deep(_, prefix, deeper, One(x)) =>
        val rest = computeTreeRemainder(prefix, deeper)
        TView(x, rest)
      case Deep(_, prefix, deeper, suffix) =>
        val suffixInit = suffix.init
        val annot = mva.mAppend3(mvaa.measure(prefix), mvat.measure(deeper), mvaa.measure(suffixInit))
        TView(suffix.last, Deep(annot, prefix, deeper, suffix.init))
    }
  }

  def viewLeft[V1 >: V, A1 >: A](implicit mva: Measured[V1, A1]): View[V1, A1] = {
    val mvaa: Measured[V1, Affix[A1]] = Measured[V1, Affix[A1]]
    val mvat: Measured[V1, FingerTree[V1, Node[V1, A1]]] = Measured[V1, FingerTree[V1, Node[V1, A1]]]

    def computeTreeRemaining(deeper: FingerTree[V1, Node[V1, A1]], suffix: Affix[A1]) = {
      val rest = deeper.viewLeft match {
        case TView(node, rest1) =>
          val pref = Affix.fromList(node.toList)
          val annot = mva.mAppend3(mvaa.measure(pref), mvat.measure(rest1), mvaa.measure(suffix))
          Deep(annot, pref, rest1, suffix)
        case Nil => FingerTree.affixToTree(suffix)
      }
      rest
    }

    this match {
      case Empty     => Nil
      case Single(x) => TView(x, Empty)
      case Deep(_, One(x), deeper, suffix) =>
        val rest: FingerTree[V1, A1] = computeTreeRemaining(deeper, suffix)
        TView(x, rest)
      case Deep(_, prefix, deeper, suffix) =>
        val first :: rest = prefix.toList
        val prefix1 = Affix.fromList(rest)
        val annot = mva.mAppend3(mvaa.measure(prefix1), mvat.measure(deeper), mvaa.measure(suffix))
        TView(first, Deep(annot, prefix1, deeper, suffix))
    }
  }

  def treeHead[V1 >: V, A1 >: A](implicit mva: Measured[V1, A1]): A1 = {
    this.viewLeft[V1, A1] match {
      case Nil         => error("no elements in the tree")
      case TView(x, _) => x
    }
  }

  def treeTail[V1 >: V, A1 >: A](implicit mva: Measured[V1, A1]): FingerTree[V1, A1] = {
    this.viewLeft[V1, A1] match {
      case Nil          => error("no elements in the tree")
      case TView(_, xs) => xs
    }
  }

  def treeLast[V1 >: V, A1 >: A](implicit mva: Measured[V1, A1]): A1 = {
    this.viewRight[V1, A1] match {
      case Nil         => error("no elements in the tree")
      case TView(x, _) => x
    }
  }

  def treeInit[V1 >: V, A1 >: A](implicit mva: Measured[V1, A1]): FingerTree[V1, A1] = {
    this.viewRight[V1, A1] match {
      case Nil          => error("no elements in the tree")
      case TView(_, xs) => xs
    }
  }

  def isEmpty: Boolean = {
    this match {
      case Empty => true
      case _     => false
    }
  }

  @inline def ><[V1 >: V, A1 >: A](that: FingerTree[V1, A1])(implicit mva: Measured[V1, A1]): FingerTree[V1, A1] = {
    this concat that
  }

  def concat[V1 >: V, A1 >: A](that: FingerTree[V1, A1])(implicit mva: Measured[V1, A1]): FingerTree[V1, A1] = {
    FingerTree.concatWithMiddle(this, List.empty, that)
  }

}

private[fingertrees] object FingerTree {

  implicit def toMeasured[A, V](implicit mav: Measured[V, A]): Measured[V, FingerTree[V, A]] = new Measured[V, FingerTree[V, A]] {
    override def measure(tree: FingerTree[V, A]): V = {
      tree match {
        case Empty         => mav.mempty
        case Single(x)     => mav.measure(x)
        case d: Deep[V, _] => d.annotation
      }
    }
    override def mempty: V = mav.mempty
    override def mappend(x: V, y: V): V = mav.mappend(x, y)
  }

  //Convert an affix into an entire tree, doing rebalancing if necessary.
  private def affixToTree[A, V](affix: Affix[A])(implicit mva: Measured[V, A]): FingerTree[V, A] = {
    val mvaa: Measured[V, Affix[A]] = Measured[V, Affix[A]]
    affix match {
      case One(x)           => Single(x)
      case Two(x, y)        => Deep(mvaa.measure(affix), One(x), Empty, One(y))
      case Three(x, y, z)   => Deep(mvaa.measure(affix), One(x), Empty, Two(y, z))
      case Four(x, y, z, w) => Deep(mvaa.measure(affix), Two(x, y), Empty, Two(z, w))
    }
  }

  private def deep[V, A](prefix: List[A], deeper: FingerTree[V, Node[V, A]], suffix: List[A])(implicit mav: Measured[V, A]): FingerTree[V, A] = {
    def annotation = {
      val tmav = Measured[V, FingerTree[V, Node[V, A]]]
      val deeperAnno: V = tmav.measure(deeper)
      val prefixAnno: V = mav.mConcat(prefix)
      val suffixAnno: V = mav.mConcat(suffix)
      mav.mAppend3(prefixAnno, deeperAnno, suffixAnno)
    }

    (prefix, suffix) match {
      case (List(), List()) =>
        deeper.viewLeft match {
          case Nil                  => Empty
          case TView(node, deeper1) => deep(node.toList, deeper1, List.empty[A])
        }
      case (List(), _) =>
        deeper.viewRight match {
          case Nil                  => affixToTree(Affix.fromList(suffix))
          case TView(node, deeper1) => deep(node.toList, deeper1, suffix)
        }
      case (_, List()) =>
        deeper.viewRight match {
          case Nil                  => affixToTree(Affix.fromList(prefix))
          case TView(node, deeper1) => deep(prefix, deeper1, node.toList)
        }
      case _ =>
        if (prefix.length > 4 || suffix.length > 4) {
          error("Affixes cannot be longer than 4 elements")
        } else {
          Deep(annotation, Affix.fromList(prefix), deeper, Affix.fromList(suffix))
        }
    }
  }

  private def concatWithMiddle[V, A](left: FingerTree[V, A], as: List[A], right: FingerTree[V, A])(implicit mva: Measured[V, A]): FingerTree[V, A] = {
    (left, as, right) match {
      case (Empty, List(), r)  => r
      case (Empty, x :: xs, r) => concatWithMiddle(Empty, xs, r) <| x
      case (Single(y), xs, r)  => concatWithMiddle(Empty, xs, r) <| y
      case (l, List(), Empty)  => l
      case (l, xs, Empty)      => concatWithMiddle(l, xs.init, Empty) |> xs.last
      case (l, xs, Single(y))  => concatWithMiddle(l, xs, Empty) |> y

      case (l: Deep[V, A], mid, r: Deep[V, A]) =>
        val mid1 = nodes(l.suffix.toList ++ mid ++ r.prefix.toList)
        val deeper1 = concatWithMiddle(l.deeper, mid1, r.deeper)
        deep(l.prefix.toList, deeper1, r.suffix.toList)
    }
  }

  private def nodes[V, A](xs: List[A])(implicit mva: Measured[V, A]): List[Node[V, A]] = {
    xs match {
      case List()         => error("not enough elements for nodes")
      case List(_)        => error("not enough elements for nodes")
      case List(x, y)     => List(Node(x, y))
      case List(x, y, z)  => List(Node(x, y, z))
      case x :: y :: rest => Node(x, y) :: nodes(rest)
    }
  }
}

//The empty tree
private[fingertrees] case object Empty extends FingerTree[Nothing, Nothing]

//Special case for trees of size one
private[fingertrees] final case class Single[V, A](x: A) extends FingerTree[V, A]

//The common case with a prefix, suffix, and link to a deeper tree.
private[fingertrees] final case class Deep[V, A](
    annotation: V,
    prefix: Affix[A],
    deeper: FingerTree[V, Node[V, A]],
    suffix: Affix[A]
) extends FingerTree[V, A]