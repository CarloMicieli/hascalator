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
import org.scalacheck.{ Arbitrary, Gen }
import Gen._
import io.hascalator.AbstractPropertySpec
import org.scalacheck.Prop.forAll
import tests.arbitrary.list._
import tests.generator.list._

class ListProperties extends AbstractPropertySpec {
  //  property("intersperse: length increased") {
  //    check(forAll { (x: Int, xs: List[Int]) =>
  //      (xs.length > 1) ==> {
  //        xs.intersperse(x).length === (xs.length + xs.length - 1)
  //      }
  //    })
  //  }

  property(":: increase the list length by 1") {
    check(forAll { (x: Int, xs: List[Int]) =>
      val res = x :: xs
      res.length === xs.length + 1
    })
  }

  property(":: add the new element as the list head") {
    check(forAll { (x: Int, xs: List[Int]) =>
      val ys = x :: xs
      ys.head === x
    })
  }
  property("map: resulting list has the same length as the original list") {
    check(forAll { (xs: List[Int]) =>
      xs.map(_ * 2).length === xs.length
    })
  }

  property("map: compose two function") {
    check(forAll { (xs: List[Int]) =>
      val f: Int => Int = _ * 2
      val g: Int => Int = _ + 42

      xs.map(f.andThen(g)) === xs.map(f).map(g)
    })
  }

  property("foldLeft: apply function to list elements") {
    check(forAll { (xs: List[Int]) =>
      xs.foldLeft(42)(_ + _) === xs.sum + 42
    })
  }

  property("foldRight: Nil and cons recreate the original list") {
    check(forAll { (xs: List[Int]) =>
      xs.foldRight(List.empty[Int])(_ :: _) === xs
    })
  }

  property("foldLeft: Nil and cons recreate the original list reversed") {
    check(forAll { (xs: List[Int]) =>
      xs.foldLeft(List.empty[Int])((ys, y) => y :: ys) === xs.reverse
    })
  }

  property("foldRight: apply function to list elements") {
    check(forAll { (xs: List[Int]) =>
      xs.foldRight(42)(_ + _) === xs.sum + 42
    })
  }

  property("foldLeft and foldRight produce the same result for associative operations") {
    check(forAll { (xs: List[Int]) =>
      xs.foldLeft(0)(_ + _) === xs.foldRight(0)(_ + _)
    })
  }

  property("isEmpty: true when length = 0") {
    check(forAll { (xs: List[Int]) =>
      xs.isEmpty === (xs.length == 0)
    })
  }

  property("nonEmpty: true when length != 0") {
    check(forAll { (xs: List[Int]) =>
      xs.nonEmpty === (xs.length != 0)
    })
  }

  property("isEmpty and nonEmpty cannot be both true for the same list") {
    check(forAll { (xs: List[Int]) =>
      xs.isEmpty === !xs.nonEmpty
    })
  }

  property("filter: resulting list length") {
    check(forAll { (xs: List[Int]) =>
      val ys = xs.filter(_ % 2 == 0)
      ys.length <= xs.length
    })
  }

  property("filter and filterNot: consider all elements") {
    check(forAll { (xs: List[Int]) =>
      val isEven = (x: Int) => x % 2 == 0
      xs.filter(isEven).length + xs.filterNot(isEven).length === xs.length
    })
  }

  property("flatMap: resulting list has the same length as the original list") {
    check(forAll { (xs: List[Int]) =>
      xs.flatMap(x => List(2 * x)).length === xs.length
    })
  }

  property("zip: resulting list is long as the shortest one") {
    check(forAll { (xs: List[Int], ys: List[Int]) =>
      val zs = xs zip ys
      zs.length === scala.math.min(xs.length, ys.length)
    })
  }

  property("zip: resulting list must have the pair of original head elements as head") {
    check(forAll(nonEmptyList[Int], nonEmptyList[Int]) {
      (xs: List[Int], ys: List[Int]) =>
        val zs = xs zip ys
        zs.head === ((xs.head, ys.head))
    })
  }

  property("zip: resulting list contains original elements") {
    check(forAll { (xs: List[Int], ys: List[Int]) =>
      val zs = xs zip ys
      val len = zs.length

      zs.map(_._1) === xs.take(len)
      zs.map(_._2) === ys.take(len)
    })
  }

  property("zip3: result list should have the same length of the shortest list") {
    check(forAll { (xs: List[Int], ys: List[Int], zs: List[Int]) =>
      val res = xs.zip3(ys, zs)
      res.length === scala.math.min(xs.length, scala.math.min(ys.length, zs.length))
    })
  }

  property("take: take all the list elements produce the same list as result") {
    check(forAll { (xs: List[Int]) =>
      xs.take(xs.length) === xs
    })
  }

  property("take: take elements until the list have them") {
    check(forAll(intLists, posNum[Int]) { (xs: List[Int], n: Int) =>
      xs.take(n).length === scala.math.min(n, xs.length)
    })
  }

  property("drop: drop 0 elements leave the list unchanged") {
    check(forAll { (xs: List[Int]) =>
      xs.drop(0) eq xs
    })
  }

  property("drop: dropping more elements that the list length produce the empty list") {
    check(forAll { (xs: List[Int]) =>
      val ys = xs.drop(1 + xs.length)
      ys.isEmpty
    })
  }

  property("reverse: apply reverse twice produce the same list") {
    check(forAll { (xs: List[Int]) =>
      xs.reverse.reverse === xs
    })
  }

  property("append: the resulting length is the sum of the two original lists") {
    check(forAll { (xs: List[Int], ys: List[Int]) =>
      (xs ++ ys).length === xs.length + ys.length
    })
  }

  property("append and ++ are equivalent") {
    check(forAll { (xs: List[Int], ys: List[Int]) =>
      (xs ++ ys) === (xs append ys)
    })
  }

  property("replicate: produce a list with length n") {
    check(forAll(posNum[Int]) { (n: Int) =>
      val ys = List.replicate(n)(42)
      ys.length === n
    })
  }

  //  property("flatten: resulting list has length with the sum of nested lists") {
  //    check(forAll { (xss: List[List[Int]]) =>
  //      val ys = xss.flatten
  //      ys.length === xss.map(_.length).sum
  //    })
  //  }

  property("all and any prop") {
    check(forAll { (xs: List[Int]) =>
      val p = (x: Int) => x % 2 == 0
      val negateP = (x: Int) => !p(x)
      xs.all(p) === !xs.any(negateP)
    })
  }

  property("span: sum of the two lists length is the same as the original") {
    check(forAll { (xs: List[Int]) =>
      val (ys, zs) = xs.partition(_ % 2 == 0)
      ys.length + zs.length === xs.length
    })
  }

  property("span: divide list using the predicate") {
    check(forAll { (xs: List[Int]) =>
      val isEven = (x: Int) => x % 2 == 0

      val (ys, zs) = xs.partition(isEven)
      ys.all(isEven) === true
      zs.any(isEven) === false
    })
  }

  property("splitAt: sum of resulting lists is equal to the original list length") {
    check(forAll { (x: Int, xs: List[Int]) =>
      val (ys, zs) = xs.splitAt(x)
      ys.length + zs.length === xs.length
    })
  }

  property("splitAt: length") {
    check(forAll { (x: Int, xs: List[Int]) =>
      val suffixLen = scala.math.min(xs.length, scala.math.max(0, x))
      val (ys, zs) = xs.splitAt(x)
      ys.length <= suffixLen
      zs.length <= (xs.length - suffixLen)
    })
  }

  property("partition: should not loose elements") {
    check(forAll { (xs: List[Int]) =>
      val (f, s) = xs.partition(_ > 0)
      f.length + s.length === xs.length
    })
  }

  property("partition: all elements in the first sublist must match the predicate") {
    check(forAll { (xs: List[Int]) =>
      val (f, _) = xs.partition(_ > 0)
      f.all(_ > 0) === true
    })
  }

  property("partition: no elements in the second sublist must match the predicate") {
    check(forAll { (xs: List[Int]) =>
      val (_, s) = xs.partition(_ > 0)
      s.all(_ <= 0) === true
    })
  }

  property("partition: respect the laws with filter") {
    check(forAll { (xs: List[Int]) =>
      val p = (n: Int) => n > 0
      val expected: (List[Int], List[Int]) = (xs filter p, xs filterNot p)
      xs.partition(p) === expected
    })
  }

  property("break: first element must contains only elements that don't match the predicate") {
    check(forAll { (xs: List[Int]) =>
      val p = (n: Int) => n > 0
      val (f, _) = xs break p
      f.all(p.negate) === true
    })
  }

  property("break: the head for second list is matching the predicate") {
    check(forAll { (xs: List[Int]) =>
      val p = (n: Int) => n > 0
      val (_, s) = xs break p
      s.isEmpty || p(s.head)
    })
  }

  def intLists(implicit a: Arbitrary[List[Int]]): Gen[List[Int]] = a.arbitrary
}
