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

import io.hascalator.AbstractPropertySpec
import org.scalacheck.Prop.forAll

class NumProperties extends AbstractPropertySpec {
  property("Int: was made instance of Num type class") {
    check(forAll { (x: Int, y: Int) =>
      val num = Num[Int]

      num.fromInteger(x) === x
      num.add(x, y) === x + y
      num.sub(x, y) === x - y
      num.mul(x, y) === x * y
      num.abs(x) >= 0
      num.mul(num.abs(x), num.signum(x)) === x
    })
  }

  property("Long: was made instance of Num type class") {
    check(forAll { (x: Long, y: Long) =>
      val num = Num[Long]

      num.add(x, y) === x + y
      num.sub(x, y) === x - y
      num.mul(x, y) === x * y
      num.abs(x) >= 0
      num.mul(num.abs(x), num.signum(x)) === x
    })
  }

  property("Float: was made instance of Num type class") {
    check(forAll { (x: Float, y: Float) =>
      val num = Num[Float]

      num.add(x, y) === x + y
      num.sub(x, y) === x - y
      num.mul(x, y) === x * y
      num.abs(x) >= 0
      num.mul(num.abs(x), num.signum(x)) === x
    })
  }

  property("Double: was made instance of Num type class") {
    check(forAll { (x: Double, y: Double) =>
      val num = Num[Double]

      num.add(x, y) === x + y
      num.sub(x, y) === x - y
      num.mul(x, y) === x * y
      num.abs(x) >= 0
      num.mul(num.abs(x), num.signum(x)) === x
    })
  }
}
