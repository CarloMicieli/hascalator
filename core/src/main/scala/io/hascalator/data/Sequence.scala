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

/** General purpose finite sequences. Apart from being finite and having strict operations, sequences also differ from
  * lists in supporting a wider variety of operations efficiently.
  *
  * An amortized running time is given for each operation, with n referring to the length of the sequence and i being
  * the integral index used by some operations. These bounds hold even in a persistent (shared) setting.
  *
  * The implementation uses 2-3 finger trees annotated with sizes, as described in section 4.2 of
  * - Ralf Hinze and Ross Paterson, "Finger trees: a simple general-purpose data structure",
  *   Journal of Functional Programming 16:2 (2006) pp 197-217. http://staff.city.ac.uk/~ross/papers/FingerTree.html
  *
  * @tparam A the element type
  * @author Carlo Micieli
  * @since 0.1
  */
trait Sequence[A] {

  /** O(1). Is this the empty sequence?
    * @return
    */
  def isEmpty: Boolean

  /** O(1). The number of elements in the sequence.
    * @return
    */
  def length: Int

  /** O(log(min(i,n-i))). The element at the specified position, counting from 0. If the specified position is
    * negative or at least the length of the sequence, lookup returns Nothing.
    * @param index
    * @return
    */
  def lookup(index: Int): Maybe[A]

  /** O(log(min(i,n-i))). The element at the specified position, counting from 0. The argument should thus be a
    * non-negative integer less than the size of the sequence. If the position is out of range, `apply` fails with an error.
    *
    * @param index
    * @return
    */
  def apply(index: Int): A

  /** O(1). Add an element to the left end of a sequence. Mnemonic: a triangle with the single element at the pointy end.
    *
    * @param x the element to add
    * @return
    */
  def <|(x: A): Seq[A]

  /** O(1). Add an element to the right end of a sequence. Mnemonic: a triangle with the single element at the pointy end.
    *
    * @param x the element to add
    * @return
    */
  def |>(x: A): Seq[A]

  /** O(log(min(n1,n2))). Concatenate two sequences.
    * @param that the other `Seq`
    * @return
    */
  def ><(that: Seq[A]): Seq[A]

  /** O(log(min(i,n-i))). Replace the element at the specified position. If the position is out of range,
    * the original sequence is returned.
    * @param index the index to update
    * @param x the new value
    * @return
    */
  def update(index: Int, x: A): Set[A]

  /** O(log(min(i,n-i))). The first `i` elements of a sequence. If `i` is negative, `s.take(i)` yields the empty sequence.
    * If the sequence contains fewer than `i` elements, the whole sequence is returned.
    *
    * @param i the number of elements to take
    * @return
    */
  def take(i: Int): Seq[A]

  /** O(log(min(i,n-i))). Elements of a sequence after the first `i`. If `i` is negative, `s.drop(i)` yields the whole
    * sequence. If the sequence contains fewer than `i` elements, the empty sequence is returned.
    * @param i the number of element to drop
    * @return
    */
  def drop(i: Int): Seq[A]

  /** O(log(min(i,n-i))). `s.insertAt(i, x)` inserts `x` into `s` at the index `i`, shifting the rest of the sequence over.
    *
    * @param i
    * @param x
    * @return
    */
  def insertAt(i: Int, x: A): Seq[A]

  /** O(log(min(i,n-i))). Delete the element of a sequence at a given index. Return the original sequence
    * if the index is out of range.
    *
    * @param i
    * @return
    */
  def deleteAt(i: Int): Seq[A]

  /** O(log(min(i,n-i))). Split a sequence at a given position. `s.splitAt(i) == (s.take(i), s.drop(i))`.
    *
    * @param i
    * @return
    */
  def splitAt(i: Int): (Seq[A], Seq[A])

  /** `foldLeftWithIndex` is a version of `foldLeft` that also provides access to the index of each element.
    *
    * @param b
    * @param f
    * @tparam B
    * @return
    */
  def foldLeftWithIndex[B](b: B)(f: (B, Int, A) => B): B

  /** `foldRightWithIndex` is a version of `foldRight` that also provides access to the index of each element.
    *
    * @param b
    * @param f
    * @tparam B
    * @return
    */
  def foldRightWithIndex[B](b: B)(f: (Int, A, B) => B): B

  /** O(n). The reverse of a sequence.
    * @return
    */
  def reverse: Seq[A]

  /** Intersperse an element between the elements of a sequence.
    * @param x
    * @return
    */
  def intersperse(x: A): Seq[A]

  /** O(min(n1,n2)). `zip` takes two sequences and returns a sequence of corresponding pairs. If one input is short,
    * excess elements are discarded from the right end of the longer sequence.
    *
    * @param that
    * @tparam B
    * @return
    */
  def zip[B](that: Seq[B]): Seq[(A, B)]

  /** O(min(n1,n2)). `zipWith` generalizes `zip` by zipping with the function given as the first argument, instead of a
    * tupling function. For example, `seq1.zipWith(seq2)(_ + _)` is applied to two sequences to take the sequence
    * of corresponding sums.
    *
    * @param that
    * @param f
    * @tparam B
    * @tparam C
    * @return
    */
  def zipWith[B, C](that: Seq[B])(f: (A, B) => C): Seq[C]
}

/** General-purpose finite sequences.
  */
object Seq {
  /** O(1). The empty sequence.
    * @tparam A
    * @return
    */
  def empty[A]: Seq[A] = undefined

  /** O(1). A singleton sequence.
    * @param x
    * @tparam A
    * @return
    */
  def singleton[A](x: A): Seq[A] = undefined

  /** O(n). Create a sequence from a finite list of elements.
    * @param xs
    * @tparam A
    * @return
    */
  def fromList[A](xs: List[A]): Seq[A] = undefined

  /** O(n). Convert a given sequence length and a function representing that sequence into a sequence.
    *
    * @param length the sequence length
    * @param f the generating function
    * @tparam A the element type
    * @return
    */
  def fromFunction[A](length: Int)(f: Int => A): Seq[A] = undefined

  /** O(log n). `replicate(n, x)` is a sequence consisting of `n` copies of `x`.
    *
    * @param n
    * @param x
    * @tparam A
    * @return
    */
  def replicate[A](n: Int, x: A): Seq[A] = undefined

  /** Builds a sequence from a seed value. Takes time linear in the number of generated elements.
    *
    * WARNING: If the number of generated elements is infinite, this method will not terminate.
    *
    * @param f
    * @param b
    * @tparam A
    * @tparam B
    * @return
    */
  def unfoldRight[A, B](f: B => Maybe[(A, B)])(b: B): Seq[B] = undefined

  /** Builds a sequence from a seed value. Takes time linear in the number of generated elements.
    *
    * @param f
    * @param b
    * @tparam A
    * @tparam B
    * @return
    */
  def unfoldLeft[A, B](f: B => Maybe[(B, A)])(b: B): Seq[B] = undefined
}