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
import org.scalacheck.Prop.forAll
import tests.arbitrary.set._

class SetProperties extends AbstractPropertySpec {
  import setSymbols._

  property("size and isEmpty are consistent for sets") {
    check(forAll { (set: Set[Int]) =>
      {
        (set.size == 0) === set.isEmpty
      }
    })
  }

  property("insert element to sets") {
    check(forAll { (set: Set[Int], x: Int) =>
      {
        val set2 = set.insert(x)
        set2.member(x) === true
      }
    })
  }

  property("union: commutativity property") {
    check(forAll { (A: Set[Int], B: Set[Int]) =>
      {
        (A ∪ B) === (B ∪ A)
      }
    })
  }

  property("union: associativity property") {
    check(forAll { (A: Set[Int], B: Set[Int], C: Set[Int]) =>
      {
        ((A ∪ B) ∪ C) === (A ∪ (B ∪ C))
      }
    })
  }

  property("union: A ⊆ (A ∪ B)") {
    check(forAll { (A: Set[Int], B: Set[Int]) =>
      {
        A ⊆ (A ∪ B)
      }
    })
  }

  property("union: A ∪ A = A") {
    check(forAll { (A: Set[Int]) =>
      {
        (A ∪ A) === A
      }
    })
  }

  property("union: A ∪ ∅ = A") {
    check(forAll { (A: Set[Int]) =>
      {
        (A ∪ ∅) === A
      }
    })
  }

  property("union: A ⊆ B if and only if A ∪ B = B.") {
    check(forAll { (A: Set[Int], B: Set[Int]) =>
      {
        (A ⊆ B) === ((A ∪ B) == B)
      }
    })
  }

  property("intersection: A ∩ B = B ∩ A.") {
    check(forAll { (A: Set[Int], B: Set[Int]) =>
      {
        (A ∩ B) === (B ∩ A)
      }
    })
  }

  property("intersection: A ∩ (B ∩ C) = (A ∩ B) ∩ C.") {
    check(forAll { (A: Set[Int], B: Set[Int], C: Set[Int]) =>
      {
        (A ∩ (B ∩ C)) === ((A ∩ B) ∩ C)
      }
    })
  }

  property("intersection: A ∩ B ⊆ A.") {
    check(forAll { (A: Set[Int], B: Set[Int]) =>
      {
        (A ∩ B) ⊆ A
      }
    })
  }

  property("intersection: A ∩ A = A.") {
    check(forAll { (A: Set[Int]) =>
      {
        (A ∩ A) === A
      }
    })
  }

  property("intersection: A ∩ ∅ = ∅.") {
    check(forAll { (A: Set[Int]) =>
      {
        (A ∩ ∅) === ∅
      }
    })
  }

  property("intersection: A ⊆ B if and only if A ∩ B = A.") {
    check(forAll { (A: Set[Int], B: Set[Int]) =>
      {
        (A ⊆ B) === ((A ∩ B) == A)
      }
    })
  }
}

object setSymbols {

  implicit class SetExtensions[T](val A: Set[T]) extends AnyVal {
    def ∪(B: Set[T])(implicit ord: Ord[T]): Set[T] = A union B
    def ∩(B: Set[T])(implicit ord: Ord[T]): Set[T] = A intersection B
    def ⊆(B: Set[T])(implicit ord: Ord[T]): Boolean = A isSubsetOf B
  }

  def ∅[A]: Set[A] = Set.empty
}