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

import io.hascalator.AbstractPropertySpec
import org.scalacheck.Prop.{ forAll }

class ListStackProperties extends AbstractPropertySpec {
  property("push and pop: get the last inserted element") {
    check(forAll { (x: Int, stack: Stack[Int]) =>
      val s = stack push x
      val (y, _) = s.pop.get
      y === x
    })
  }

  property("push increase size by 1") {
    check(forAll { (x: Int, stack: Stack[Int]) =>
      val s = stack push x
      s.size === stack.size + 1
    })
  }

  //  property("top: return the top element") {
  //    check(forAll { (stack: Stack[Int]) =>
  //      stack.nonEmpty ==> {
  //        val x = stack.top.get
  //        val (topEl, _) = stack.pop.get
  //        x === topEl
  //      }
  //    })
  //  }
}
