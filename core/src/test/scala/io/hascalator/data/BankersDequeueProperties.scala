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

import org.scalacheck.Prop.forAll
import tests.arbitrary.bankersDequeue._
import tests.arbitrary.list._

class BankersDequeueProperties extends AbstractPropertySpec {
  property("if you push, then pop, the front of the queue, you get the same queue") {
    check(forAll { (x: Int, q: BankersDequeue[Int]) =>
      val Just((f, q2)) = q.pushFront(x).popFront
      f === x
      q === q2
    })
  }

  property("if you push, then pop, the back of the queue, you get the same queue") {
    check(forAll { (x: Int, q: BankersDequeue[Int]) =>
      val Just((_, q2)) = q.pushBack(x).popBack
      q.toList === q2.toList
    })
  }

  property("first returns the last pushFront'd element") {
    check(forAll { (x: Int, xs: List[Int], q: BankersDequeue[Int]) =>
      val q2 = (x :: xs).foldLeft(q)((dq, e) => dq.pushFront(e))
      q2.first === Maybe.just((x :: xs).last)
    })
  }

  property("length of a queue is the same as the length of the list generated from the queue") {
    check(forAll { (xs: List[Int]) =>
      BankersDequeue.fromList(xs).length === xs.length
    })
  }

  property("fromList . toList is the identity") {
    check(forAll { (xs: List[Int]) =>
      BankersDequeue.fromList(xs).toList === xs
    })
  }

}
