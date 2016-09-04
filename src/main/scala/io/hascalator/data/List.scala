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

import scala.collection.mutable
import scala.inline

/** A list is either empty, or a constructed list with a `head` and a `tail`.
  * @tparam A the list element type
  *
  * @author Carlo Micieli
  * @since 0.0.1
  */
sealed trait List[+A] {
  self =>

  /** Returns the first element of a list.
    * @return the first element
    */
  def head: A

  /** Optionally returns the first element of a list.
    * @return ''just(head)'' if the list is not empty; ''none'' otherwise
    */
  def headMaybe: Maybe[A] = {
    import Maybe._
    if (isEmpty) {
      none
    } else {
      just(head)
    }
  }

  /** Extract the last element of a list, which must be finite and non-empty.
    * @return the last element for non empty lists
    */
  def last: A = {
    this match {
      case x :: Nil  => x
      case _ :: rest => rest.last
      case Nil       => error("List.last: empty list")
    }
  }

  /** Returns the elements after the head of a list.
    * @return the elements after the head
    */
  def tail: List[A]

  /** Return all the elements of a list except the last one. The list must be non-empty.
    * @return the list elements, but the last one
    */
  def init: List[A] = {
    if (isEmpty) {
      error("List.init: empty list")
    } else {
      val builder = new ListBuilder[A]

      @tailrec
      def loop(xs: List[A]): Unit = {
        xs match {
          case x :: Nil =>
            ()
          case h :: t =>
            builder += h
            loop(t)
        }
      }

      loop(this)
      builder.result()
    }
  }

  /** Checks whether this list is empty.
    * @return `true` if the list is empty; `false` otherwise
    */
  def isEmpty: Boolean

  /** Checks whether this list is not empty.
    * @return `true` if the list is not empty; `false` otherwise
    */
  def nonEmpty: Boolean = !isEmpty

  /** Adds an element at the beginning of this list.
    *
    * @usecase def ::(x: A): List[A]
    * @inheritdoc
    * @param x the element to add
    * @tparam A1 the list element type
    * @return a new list, with the element appended
    */
  @inline def ::[A1 >: A](x: A1): List[A1] = Cons(x, this)

  /** Decompose a list into its head and tail. If the list is empty, returns a ''None''.
    * If the list is non-empty, returns ''just (x, xs)'', where `x` is the head of the list
    * and `xs` its tail.
    * @return optionally a pair with the list head and tail
    */
  def unCons: Maybe[(A, List[A])] = {
    import Maybe._
    if (isEmpty) {
      none
    } else {
      just((head, tail))
    }
  }

  /** Checks whether this list contains a given value as an element.
    *
    * @usecase def elem(x: A): Boolean
    * @inheritdoc
    * @param x the element to find
    * @tparam A1 the element type
    * @return `true` if `x` is a list element; `false` otherwise
    */
  def elem[A1 >: A](x: A1)(implicit E: Eq[A1]): Boolean = {
    find(E.eq(_: A1, x)).isDefined
  }

  /** Takes a predicate and a structure and returns the leftmost element of the structure matching the predicate, or
    * ''None'' if there is no such element.
    *
    * @usecase def find(x: A): Maybe[A]
    * @inheritdoc
    * @param p the predicate to match
    * @tparam A1 the element type
    * @return `Just(x)` if the value is found; `None` otherwise
    */
  def find[A1 >: A](p: A1 => Boolean): Maybe[A1] = {
    this match {
      case y :: _ if p(y) => Maybe.just(y)
      case _ :: ys        => ys find p
      case Nil            => Maybe.none
    }
  }

  /** Returns the number of elements in the list.
    * @return the number of elements
    */
  def length: Int = {
    foldLeft(0)((len, x) => len + 1)
  }

  /** Selects all elements of this list which satisfy a predicate.
    * @param p the predicate to match
    * @return a list
    */
  def filter(p: A => Boolean): List[A] = {
    val builder = new ListBuilder[A]

    @tailrec
    def loop(xs: List[A]): Unit = {
      xs match {
        case h :: t =>
          if (p(h)) {
            builder += h
          } else {
            ()
          }
          loop(t)
        case Nil => ()
      }
    }

    loop(this)
    builder.result()
  }

  /** Selects all elements of this list which do not satisfy a predicate.
    * @param p the predicate
    * @return a list
    */
  def filterNot(p: A => Boolean): List[A] = {
    val notP = (x: A) => !p(x)
    filter(notP)
  }

  /** Returns the list obtained by applying `f` to each element of this list.
    * @param f the function to apply
    * @tparam B the resulting list elements type
    * @return the list obtained applying `f`
    */
  def map[B](f: A => B): List[B] = {
    val builder = new ListBuilder[B]

    @tailrec
    def loop(xs: List[A]): Unit = {
      xs match {
        case Cons(h, t) =>
          builder += f(h)
          loop(t)
        case Nil => ()
      }
    }

    loop(this)
    builder.result()
  }

  /** Builds a new list by applying a function to all elements and using the
    * elements of the resulting lists.
    * @param f the function to apply
    * @tparam B the resulting list elements type
    * @return the list obtained applying `f`
    */
  def flatMap[B](f: A => List[B]): List[B] = {
    val builder = new ListBuilder[B]

    @tailrec
    def loop(xs: List[A]): Unit = {
      xs match {
        case Cons(h, t) =>
          f(h).foreach(e => builder += e)
          loop(t)
        case Nil => ()
      }
    }

    loop(this)
    builder.result()
  }

  /** Returns a new list obtained appending the elements from `that` list to this one.
    *
    * @usecase def ++(that: List[A]): List[A]
    * @inheritdoc
    * @param that the second list to append
    * @tparam A1 the resulting list type
    * @return a list obtained appending the element of `that` to this list
    */
  @inline def ++[A1 >: A](that: List[A1]): List[A1] = {
    this append that
  }

  /** Returns a new list obtained appending the elements from `that` list to this one.
    *
    * @usecase def append(that: List[A]): List[A]
    * @inheritdoc
    * @param that the second list to append
    * @tparam A1 the resulting list type
    * @return a list obtained appending the element of `that` to this list
    */
  def append[A1 >: A](that: List[A1]): List[A1] = {
    (this, that) match {
      case (Nil, ys) => ys
      case (xs, Nil) => xs
      case _         => foldRight(that)((x, xs) => x :: xs)
    }
  }

  /** Applies a function `f` to all elements of this list.
    * @param f the function to apply
    * @tparam U
    * @usecase def foreach(f: A => Unit): Unit
    * @inheritdoc
    */
  def foreach[U](f: A => U): Unit = {
    var list = this
    while (list.nonEmpty) {
      val head :: rest = list
      f(head)
      list = rest
    }
  }

  def withFilter(p: A => Boolean): WithFilter = new WithFilter(p)

  //TODO: not implementing WithFilter contract right now!
  class WithFilter(p: A => Boolean) {
    def flatMap[B](f: A => List[B]): List[B] = self filter p flatMap f
    def foreach[U](f: A => U): Unit = self filter p foreach f
    def map[B](f: A => B): List[B] = self filter p map f
    def withFilter(q: A => Boolean): WithFilter = new WithFilter(x => p(x) && q(x))
  }

  /** Is the right-to-left dual of [[scanLeft]].
    *
    * Note that
    * {{{
    * xs.scanRight(zero)(f).head === xs.foldRight(zero)(f)
    * }}}
    * @param z the start value
    * @param f the binary operator
    * @tparam B the result type of the binary operator
    * @return
    */
  def scanRight[B](z: B)(f: (A, B) => B): List[B] = {
    var out = List(z)

    foldRight(z)((a, b) => {
      val next = f(a, b)
      out = next :: out
      next
    })

    out
  }

  /** Applies a binary operator to a start value and all elements of this sequence, going right to left.
    * @param z the start value
    * @param f the binary operator
    * @tparam B the result type of the binary operator
    * @return right-associative fold of this list
    */
  def foldRight[B](z: B)(f: (A, B) => B): B = {
    reverse.foldLeft(z)((xs, x) => f(x, xs))
  }

  /** A variant of [[foldRight]] that has no base case, and thus may only be applied to non-empty structures.
    * @param f the binary operator
    * @tparam A1
    * @return
    */
  def foldRight1[A1 >: A](f: (A, A1) => A1): A1 = {
    this match {
      case h :: t => t.foldRight[A1](h)(f)
      case _      => error("List.foldRight1: empty list")
    }
  }

  /** Similar to [[foldLeft]], but returns a list of successive reduced values from the left.
    *
    * Note that:
    * {{{
    * xs.scanLeft(zero)(f).last === xs.foldLeft(zero)(f)
    * }}}
    * @param z
    * @param f
    * @tparam B
    * @return
    */
  def scanLeft[B](z: B)(f: (B, A) => B): List[B] = {
    val builder = new ListBuilder[B]
    builder += z

    this.foldLeft(z)((b, a) => {
      val next = f(b, a)
      builder += next
      next
    })

    builder.result()
  }

  /** Applies a binary operator to a start value and all elements of this sequence, going left
    * to right.
    * @param z the start value
    * @param f the binary operator
    * @tparam B the result type of the binary operator
    * @return left-associative fold of this list
    */
  def foldLeft[B](z: B)(f: (B, A) => B): B = {
    @tailrec
    def go(xs: List[A], acc: B): B = xs match {
      case Nil     => acc
      case y :: ys => go(ys, f(acc, y))
    }

    go(this, z)
  }

  /** A variant of [[foldLeft]] that has no base case, and thus may only be applied to non-empty structures.
    * @usecase def foldLeft1(f: (A, A) => A): A
    * @inheritdoc
    * @param f the binary operator
    * @tparam A1 the result type
    * @return left-associative fold of this list
    */
  def foldLeft1[A1 >: A](f: (A1, A) => A1): A1 = {
    unCons.map {
      case (h, t) => t.foldLeft[A1](h)(f)
    } getOrElse {
      error("List.foldLeft1: empty list")
    }
  }

  /** Returns the elements of this list in reverse order.
    * @return the list in reversed order
    */
  def reverse: List[A] = {
    foldLeft(List.empty[A])((xs, x) => x :: xs)
  }

  /** Returns the prefix of this list of length `m`, or the list itself if `m > length`.
    *
    * ===Examples===
    * {{{
    * scala> List(1, 2, 3, 4, 5) take 3
    * res0: List[Int] = [1,2,3]
    *
    * scala> List(1, 2) take 3
    * res1: List[Int] = [1,2]
    *
    * scala> List.empty[Int] take 3
    * res2: List[Int] = []
    *
    * scala> List(1, 2, 3) take -1
    * res3: List[Int] = []
    *
    * scala> List(1, 2, 3) take 0
    * res4: List[Int] = []
    * }}}
    *
    * @param m the number of elements to take
    * @return the list prefix of length `m`
    */
  def take(m: Int): List[A] = {
    val builder = new ListBuilder[A]

    @tailrec
    def loop(xs: List[A], i: Int): Unit = {
      xs match {
        case _ if i <= 0 => ()
        case Nil         => ()
        case h :: t =>
          builder += h
          loop(t, i - 1)
      }
    }
    loop(this, m)
    builder.result()
  }

  /** Takes longest prefix of this list that satisfy a predicate.
    *
    * ===Examples===
    * {{{
    * scala> List(1,2,3,4,1,2,3,4) takeWhile (_ < 3)
    * res0: List[Int] = [1, 2]
    *
    * scala> List(1, 2, 3) takeWhile (_ < 9)
    * res1: List[Int] = [1, 2, 3]
    *
    * scala> List(1, 2, 3) takeWhile (_ < 0)
    * res2: List[Int] = []
    * }}}
    *
    * @param p the predicate to match
    * @return the longest prefix that satisfy `p`
    */
  def takeWhile(p: (A) ⇒ Boolean): List[A] = {
    val builder = new ListBuilder[A]

    @tailrec
    def loop(xs: List[A]): Unit = {
      xs match {
        case h :: t =>
          if (p(h)) {
            builder += h
            loop(t)
          } else {
            ()
          }
        case _ => ()
      }
    }

    loop(this)
    builder.result()
  }

  /** Returns the suffix of this list of length `m`, or the empty list if `m > length`.
    *
    * ===Examples===
    * {{{
    * scala> List(1, 2, 3, 4, 5) drop 3
    * res0: List[Int] = [4,5]
    *
    * scala> List(1, 2) drop 3
    * res1: List[Int] = []
    *
    * scala> List.empty[Int] drop 3
    * res2: List[Int] = []
    *
    * scala> List(1, 2) drop -1
    * res3: List[Int] = [1,2]
    *
    * scala>  List(1, 2) drop 0
    * res4: List[Int] = [1,2]
    * }}}
    *
    * @param m the number of elements to drop
    * @return the list suffix of length `m`
    */
  @tailrec final def drop(m: Int): List[A] = this match {
    case Nil          => Nil
    case xs if m == 0 => xs
    case _ :: xs      => xs.drop(m - 1)
  }

  /** Drops longest prefix of this list that satisfy a predicate.
    *
    * ===Examples===
    * {{{
    * scala> List(1,2,3,4,5,1,2,3) dropWhile (_ < 3)
    * res0: List[Int] = [3, 4, 5, 1, 2, 3]
    *
    * scala> List(1,2,3) dropWhile (_ < 9)
    * res1: List[Int] = []
    *
    * scala> List(1,2,3) dropWhile (_ < 0)
    * res2: List[Int] = [1,2,3]
    * }}}
    *
    * @param p the predicate to match
    * @return the suffix, if any
    */
  @tailrec final def dropWhile(p: A => Boolean): List[A] = {
    this match {
      case Nil              => Nil
      case x :: xs if !p(x) => this
      case _ :: xs          => xs.dropWhile(p)
    }
  }

  /** Drops the largest suffix of a list in which the given predicate holds for all elements.
    *
    * ===Examples===
    * {{{
    * scala> List(1,2,3,4,5) dropWhileEnd (_ < 3)
    * res0: List[Int] = [1,2,3,4,5]
    *
    * scala> List(1,2,3,4,5) dropWhileEnd (_ > 10)
    * res1: List[Int] = [1,2,3,4,5]
    *
    * scala> List(1,2,3,4,5) dropWhileEnd (_ < 20)
    * res2: List[Int] = []
    * }}}
    *
    * @param p
    * @return
    */
  def dropWhileEnd(p: A => Boolean): List[A] = undefined

  /** Returns a tuple where first element is the prefix of length `m`
    * and second element is the remainder of the list.
    *
    * ===Examples===
    * {{{
    * scala> List(1, 2, 3, 4, 5) splitAt 3
    * res0: Tuple2[List[Int], List[Int]] = ([1, 2, 3], [4, 5])
    *
    * scala> List(1, 2, 3) splitAt 1
    * res1: Tuple2[List[Int], List[Int]] = ([1], [2, 3])
    *
    * scala> List(1, 2, 3) splitAt 3
    * res2: Tuple2[List[Int], List[Int]] = ([1, 2, 3], [])
    *
    * scala> List(1, 2, 3) splitAt 4
    * res3: Tuple2[List[Int], List[Int]] = ([1, 2, 3], [])
    *
    * scala> List(1, 2, 3) splitAt 0
    * res4: Tuple2[List[Int], List[Int]] = ([], [1, 2, 3])
    *
    * scala> List(1, 2, 3) splitAt -1
    * res5: Tuple2[List[Int], List[Int]] = ([], [1, 2, 3])
    * }}}
    *
    * @param m the index where the list will be split
    * @return a pair of lists
    */
  def splitAt(m: Int): (List[A], List[A]) = {
    val builder = new ListBuilder[A]

    @tailrec
    def loop(n: Int, xs: List[A]): List[A] = {
      (n, xs) match {
        case (i, _) if i < 0   => xs
        case (0, _) | (_, Nil) => xs
        case (i, h :: t) =>
          builder += h
          loop(n - 1, t)
      }
    }
    val rest = loop(m, this)
    (builder.result(), rest)
  }

  /** `O(n)` Partitions this `List` in two lists according to the given predicate.
    *
    * The following equivalence must always hold:
    * {{{
    * (xs partition p) === (xs filter p, xs filterNot p)
    * }}}
    *
    * @param p the predicate to match
    * @return a pair of Lists
    */
  def partition(p: A => Boolean): (List[A], List[A]) = {
    this match {
      case Nil => (Nil, Nil)
      case x :: xs =>
        val fstBuilder = new ListBuilder[A]
        val sndBuilder = new ListBuilder[A]

        this.foreach(h =>
          if (p(h)) {
            fstBuilder += h
          } else {
            sndBuilder += h
          })

        (fstBuilder.result(), sndBuilder.result())
    }
  }

  /** Returns a tuple where first element is longest prefix (possibly empty) of this list elements that satisfy `p` and
    * second element is the remainder of the list.
    *
    * ===Examples===
    * {{{
    * scala> List(1,2,3,4,1,2,3,4) span (_ < 3)
    * res0: Tuple2[List[Int], List[Int]] = ([1,2], [3,4,1,2,3,4])
    *
    * scala> List(1, 2, 3) span (_ < 9)
    * res1: Tuple2[List[Int], List[Int]] = ([1,2,3],[])
    *
    * scala> List(1, 2, 3) span (_ < 0)
    * res2: ([], [1,2,3])
    * }}}
    * @param p the predicate to apply
    * @return a pair of lists.
    */
  def span(p: A => Boolean): (List[A], List[A]) = {
    val builder = new ListBuilder[A]

    @tailrec
    def loop(xs: List[A]): List[A] = {
      xs match {
        case h :: t if p(h) =>
          builder += h
          loop(t)
        case _ => xs
      }
    }

    val rest = loop(this)
    (builder.result(), rest)
  }

  /** Returns a tuple where first element is longest prefix (possibly empty) of this list elements that do not satisfy
    * `p` and second element is the remainder of the list.
    *
    * ===Examples===
    * {{{
    * scala> List(1,2,3,4,1,2,3,4) break (_ > 3)
    * ([1, 2, 3], [4, 1, 2, 3, 4])
    *
    * scala> List(1, 2, 3) break (_ < 9)
    * ([],[1, 2, 3])
    *
    * scala> List(1, 2, 3) break (_ > 9)
    * ([1, 2, 3], [])
    * }}}
    *
    * @param p
    * @return
    */
  def break(p: A => Boolean): (List[A], List[A]) = {
    val builder = new ListBuilder[A]

    @tailrec
    def loop(xs: List[A]): List[A] = {
      xs match {
        case h :: t if !p(h) =>
          builder += h
          loop(t)
        case _ => xs
      }
    }

    val remaining = loop(this)
    (builder.result(), remaining)
  }

  /** Drops the given prefix from a list.
    *
    * It returns a ''None'' if the list did not start with the prefix given, or a ''Just'' wrapping the list after
    * the prefix, if it does.
    *
    * ===Examples===
    * {{{
    * scala> List('f', 'o', 'o', 'b', 'a', 'r') stripPrefix List('f', 'o', 'o')
    * res0: Maybe[List[Char]] = Just([b, a, r])
    *
    * scala> List('f', 'o', 'o') stripPrefix List('f', 'o', 'o')
    * res1: Maybe[List[Char]] = Just([])
    *
    * scala> List('b', 'a', 'r', 'f', 'o', 'o') stripPrefix List('f', 'o', 'o')
    * res2: Maybe[List[Char]] = None
    *
    * scala> List('b', 'a', 'r', 'f', 'o', 'o', 'b', 'a', 'z') stripPrefix List('f', 'o', 'o')
    * res3: Maybe[List[Char]] = None
    * }}}
    *
    * @usecase def stripPrefix(prefix: List[A]): Maybe[List[A]]
    * @inheritdoc
    * @param prefix the list prefix to strip
    * @param e
    * @tparam A1 the resulting list element type
    * @return optionally the list after the prefix stripped
    */
  def stripPrefix[A1 >: A](prefix: List[A1])(implicit e: Eq[A1]): Maybe[List[A1]] = {
    @tailrec
    def loop(p: List[A1], l: List[A1]): Maybe[List[A1]] = {
      (p, l) match {
        case (Nil, list)   => Maybe.just(list)
        case (_ :: _, Nil) => Maybe.none
        case (a :: as, b :: bs) =>
          if (Eq[A1].eq(a, b)) {
            loop(as, bs)
          } else {
            Maybe.none
          }
      }
    }

    loop(prefix, this)
  }

  /** Returns the concatenation of all the elements of a container of lists.
    * @usecase def concat: List[A]
    * @inheritdoc
    * @param ev
    * @tparam B
    * @return
    */
  def concat[B](implicit ev: A => List[B]): List[B] = {
    foldRight(List.empty[B])((xss, xs) => xss ++ xs)
  }

  /** Map a function over all the elements of a container and concatenate the resulting lists.
    * @param f the function to apply
    * @tparam B the result type
    * @return
    */
  @inline def concatMap[B](f: A => List[B]): List[B] = {
    this flatMap f
  }

  /** Takes two lists and returns a list of corresponding pairs. If one input
    * list is short, excess elements of the longer list are discarded.
    * @param that the second list
    * @tparam B the second list element type
    * @return a list with corresponding pairs
    */
  def zip[B](that: List[B]): List[(A, B)] = {
    (this zipWith that)((_, _))
  }

  /** Takes three lists and returns a list of triples, analogous to [[zip]].
    * @param bs the second list
    * @param cs the third list
    * @tparam B the second list element type
    * @tparam C the third list element type
    * @return a list of triples
    */
  def zip3[B, C](bs: List[B], cs: List[C]): List[(A, B, C)] = {
    val builder = new ListBuilder[(A, B, C)]

    @tailrec
    def loop(as: List[A], bs: List[B], cs: List[C]): Unit = {
      (as, bs, cs) match {
        case (Nil, _, _) | (_, Nil, _) | (_, _, Nil) => ()
        case (x :: xs, y :: ys, z :: zs) =>
          val el = (x, y, z)
          builder += el
          loop(xs, ys, zs)
      }
    }

    loop(this, bs, cs)
    builder.result()
  }

  /** `O(n)` Takes two lists and returns a list applying `f` to each corresponding pair. If one input
    * list is short, excess elements of the longer list are discarded.
    * @param that the second list
    * @param f the function to produce elements in the resulting list
    * @tparam B the second list element type
    * @tparam C the resulting list element type
    * @return a list
    */
  def zipWith[B, C](that: List[B])(f: (A, B) => C): List[C] = {
    val builder = new ListBuilder[C]

    def loop(fst: List[A], snd: List[B]): Unit = {
      (fst, snd) match {
        case (_, Nil) | (Nil, _) => ()
        case (x :: xs, y :: ys) =>
          builder += f(x, y)
          loop(xs, ys)
      }
    }

    loop(this, that)
    builder.result()
  }

  /** `O(n)` Determines whether all elements of this list satisfy the predicate.
    * @param p the predicate to match
    * @return `true` if all elements match the predicate; `false` otherwise
    */
  def all(p: A => Boolean): Boolean = this match {
    case Nil     => true
    case x :: xs => p(x) && xs.all(p)
  }

  /** `O(n)` Determines whether any elements of this list satisfy the predicate.
    * @param p the predicate to match
    * @return `true` if any elements match the predicate; `false` otherwise
    */
  def any(p: A => Boolean): Boolean = this match {
    case Nil     => false
    case x :: xs => p(x) || xs.any(p)
  }

  /** `O(n)` Takes an element and a list and "intersperses" that element between the elements of the list.
    *
    * Example:
    * {{{
    * scala> val l = List('a', 'b', 'c', 'd', 'e').intersperse('-')
    * l: List[Char] = [a, -, b, -, c, -, d, -, e]
    * }}}
    *
    * @usecase def intersperse(x: A): List[A]
    * @inheritdoc
    * @param x
    * @tparam A1
    * @return
    */
  def intersperse[A1 >: A](x: A1): List[A1] = {
    this match {
      case h :: t =>
        val builder = new ListBuilder[A1]
        builder += h
        t.foreach(h1 => builder ++= Seq(x, h1))
        builder.result()
      case _ => this
    }
  }

  /** `O(n)` Displays all elements of this list in a string.
    * @param sep the elements separator
    * @param start the starting element
    * @param end the ending element
    * @return a string
    */
  def mkString(sep: String, start: String = "", end: String = ""): String = {
    val itemsString =
      if (isEmpty) {
        ""
      } else {
        tail.foldLeft(head.toString)((str, x) => str + sep + x)
      }
    s"$start$itemsString$end"
  }

  override def toString: String = mkString(", ", "[", "]")
}

object List extends ListInstances {
  /** Creates a new, empty `List`.
    * @tparam A the list element type
    * @return an empty `List`
    */
  def empty[A]: List[A] = Nil

  /** Creates a list with the specified elements.
    * @param items the list elements
    * @tparam A the list element type
    * @return a list
    */
  def apply[A](items: A*): List[A] = {
    if (items.isEmpty) {
      List.empty[A]
    } else {
      items.head :: apply(items.tail: _*)
    }
  }

  /** This function is a "dual" to `foldRight`: while `foldRight` reduces a list to a summary value,
    * `unfoldRight` builds a list from a seed value.
    *
    * The function takes the element and returns `None` if it is done producing the list or returns
    * `Just (a, b)`, in which case, a is a prepended to the list and b is used as the next element in a
    * recursive call.
    *
    * @param z the initial seed
    * @param f the function to build the list
    * @tparam A the list element type
    * @tparam B the seed element type
    * @return a list
    */
  def unfoldRight[A, B](z: B)(f: B => Maybe[(A, B)]): List[A] = {
    @tailrec
    def loop(z: B, acc: List[A]): List[A] = f(z) match {
      case None           => acc
      case Just((na, nb)) => loop(nb, na :: acc)
    }

    loop(z, List.empty[A])
  }

  /** Returns a list of length `n` with `el` the value of every element.
    * @param n the number of elements
    * @param el the only element in the list
    * @tparam A the list element type
    * @return a list
    */
  def replicate[A](n: Int)(el: => A): List[A] = {
    if (n <= 0) {
      Nil
    } else {
      val x = el
      (1 to n).foldLeft(List.empty[A])((ys, y) => x :: ys)
    }
  }

  def unapplySeq[A](xs: List[A]): scala.Option[Seq[A]] = {
    scala.Some(xs.foldRight(Seq.empty[A])((x, sq) => x +: sq))
  }
}

trait ListInstances {
  implicit def toShowList[A: Show]: Show[List[A]] = Show {
    (x: List[A]) => Show[A].showList(x)
  }

  implicit def toOrdList[A: Ord](implicit ordA: Ord[A]): Ord[List[A]] = {
    @tailrec
    def compareLists(xs: List[A], ys: List[A]): Ordering = {
      (xs, ys) match {
        case (Nil, Nil)    => Ordering.EQ
        case (Nil, _ :: _) => Ordering.LT
        case (_ :: _, Nil) => Ordering.GT
        case (x :: x1, y :: y1) =>
          val cmp = ordA.compare(x, y)
          if (cmp == Ordering.EQ) {
            compareLists(x1, y1)
          } else {
            cmp
          }
      }
    }

    Ord(compareLists _)
  }
}

private[this] case class Cons[A] private (head: A, private[data] var _tail: List[A]) extends List[A] {
  override def isEmpty: Boolean = false
  override def tail: List[A] = _tail
}

object :: {
  def unapply[A](xs: List[A]): scala.Option[(A, List[A])] =
    if (xs.isEmpty) {
      scala.None
    } else {
      scala.Some((xs.head, xs.tail))
    }
}

private[this] case object Nil extends List[Nothing] {
  override def head: Nothing = error("List.head: empty list")
  override def tail: Nothing = error("List.tail: empty list")
  override def isEmpty: Boolean = true
}

final private[this] class ListBuilder[A] extends mutable.Builder[A, List[A]] {

  private var out: List[A] = List.empty
  private var end: Cons[A] = _

  override def +=(elem: A): ListBuilder.this.type = {
    out match {
      case Cons(h, t) =>
        val newEnd = Cons(elem, List.empty)
        end._tail = newEnd
        end = newEnd
        this
      case _ =>
        end = Cons(elem, List.empty)
        out = end
        this
    }
  }

  override def clear(): Unit = {
    out = List.empty
    end = null
  }

  override def result(): List[A] = out
}
