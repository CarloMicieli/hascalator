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
import io.hascalator.data.fingertrees.Value

final class FingerTreeSeq[A](tree: fingertrees.Seq[A]) extends Sequence[A] {
  override def isEmpty: Boolean = tree.isEmpty

  override def length: Int = tree.size.getSize

  override def lookup(index: Int): Maybe[A] = undefined

  override def <|(x: A): Sequence[A] = FingerTreeSeq(tree.prepend(Value(x)))

  override def |>(x: A): Sequence[A] = FingerTreeSeq(tree.append(Value(x)))

  override def ><(that: Sequence[A]): Sequence[A] = undefined

  override def update(index: Int, x: A): Set[A] = undefined

  override def take(i: Int): Sequence[A] = undefined

  override def drop(i: Int): Sequence[A] = undefined

  override def insertAt(i: Int, x: A): Sequence[A] = undefined

  override def deleteAt(i: Int): Sequence[A] = undefined

  override def splitAt(i: Int): (Sequence[A], Sequence[A]) = undefined

  override def foldLeftWithIndex[B](b: B)(f: (B, Int, A) => B): B = undefined

  override def foldRightWithIndex[B](b: B)(f: (Int, A, B) => B): B = undefined

  override def reverse: Sequence[A] = undefined

  override def intersperse(x: A): Sequence[A] = undefined

  override def zip[B](that: Sequence[B]): Sequence[(A, B)] = undefined

  override def zipWith[B, C](that: Sequence[B])(f: (A, B) => C): Sequence[C] = undefined

  override def apply(index: Int): A = undefined
}

object FingerTreeSeq {
  private def apply[A](tree: fingertrees.Seq[A]): FingerTreeSeq[A] = {
    new FingerTreeSeq[A](tree)
  }

  def empty[A]: FingerTreeSeq[A] = new FingerTreeSeq(fingertrees.emptySeq[A])
}