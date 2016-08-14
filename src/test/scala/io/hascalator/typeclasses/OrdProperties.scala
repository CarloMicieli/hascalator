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

package io.hascalator.typeclasses

import org.scalacheck.Prop.forAll
import io.hascalator.AbstractPropertySpec

class OrdProperties extends AbstractPropertySpec with OrdLaws {
  property("Ord[Char] instance is lawful") {
    check(forAll { (x: Char, y: Char, z: Char) =>
      checkAllLaws(x, y, z)
    })
  }

  property("Ord[Int] instance is lawful") {
    check(forAll { (x: Int, y: Int, z: Int) =>
      checkAllLaws(x, y, z)
    })
  }

  property("Ord[Byte] instance is lawful") {
    check(forAll { (x: Byte, y: Byte, z: Byte) =>
      checkAllLaws(x, y, z)
    })
  }

  property("Ord[Short] instance is lawful") {
    check(forAll { (x: Short, y: Short, z: Short) =>
      checkAllLaws(x, y, z)
    })
  }

  property("Ord[Float] instance is lawful") {
    check(forAll { (x: Float, y: Float, z: Float) =>
      checkAllLaws(x, y, z)
    })
  }

  property("Ord[Double] instance is lawful") {
    check(forAll { (x: Double, y: Double, z: Double) =>
      checkAllLaws(x, y, z)
    })
  }

  property("Ord[Long] instance is lawful") {
    check(forAll { (x: Long, y: Long, z: Long) =>
      checkAllLaws(x, y, z)
    })
  }

  property("Ord[Boolean] instance is lawful") {
    check(forAll { (x: Boolean, y: Boolean, z: Boolean) =>
      checkAllLaws(x, y, z)
    })
  }

  property("Ord[String] instance is lawful") {
    check(forAll { (x: String, y: String, z: String) =>
      checkAllLaws(x, y, z)
    })
  }

  def checkAllLaws[A: Ord](x: A, y: A, z: A): Boolean = {
    antisymmetryLaw(x, y)
    transitivityLaw(x, y, z)
    totalityLaw(x, y)
  }
}
