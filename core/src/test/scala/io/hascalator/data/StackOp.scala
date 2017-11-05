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

/** @tparam A
  * @author Carlo Micieli
  * @since 0.0.1
  */
sealed trait StackOp[+A]
case object PopOp extends StackOp[Nothing]
final case class PushOp[A](el: A) extends StackOp[A]
final case class InvalidStackOperation[A](stack: Stack[A], op: StackOp[A], ex: Exception)

object StackOp {
  @tailrec
  final def sequence[A](initial: Stack[A], ops: List[StackOp[A]]): Either[InvalidStackOperation[A], Stack[A]] = {
    ops match {
      case Nil               => Either.right(initial)
      case PushOp(k) :: tail => sequence(initial.push(k), tail)
      case PopOp :: tail =>
        initial.pop match {
          case Right((_, newStack)) =>
            sequence(newStack, tail)
          case Left(err) =>
            Either.left(InvalidStackOperation(initial, PopOp, err))
        }
    }
  }
}