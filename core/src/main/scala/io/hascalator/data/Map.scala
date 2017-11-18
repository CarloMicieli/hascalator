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

/** @tparam K
  * @tparam V
  * @author Carlo Micieli
  * @since 0.0.1
  */
trait Map[+K, +V] {
  /** Checks whether this map is empty.
    * @return
    */
  def isEmpty: Boolean

  /** Return the number of elements in this map.
    * @return
    */
  def size: Int

  /** Check whether x is a key contained in this map.
    * @param x
    * @tparam K1
    * @return
    */
  def contains[K1 >: K](x: K1)(implicit ord: Ord[K1]): Boolean

  /** Lookup the value at a key in the map.
    *
    * The function will return the corresponding value as (Just value),
    * or None if the key isn't in the map.
    * @param x
    * @param ord
    * @tparam K1
    * @return
    */
  def lookup[K1 >: K](x: K1)(implicit ord: Ord[K1]): Maybe[V]

  /** The expression (findWithDefault def k map) returns the value at key k or returns
    * default value def when the key is not in the map.
    * @param x
    * @param default
    * @param ord
    * @tparam K1
    * @tparam V1
    * @return
    */
  def findWithDefault[K1 >: K, V1 >: V](x: K1, default: => V1)(implicit ord: Ord[K1]): V

  /** Insert a new key and value in the map. If the key is already present in the map, the
    * associated value is replaced with the supplied value.
    * @param k
    * @param v
    * @param ord
    * @tparam K1
    * @tparam V1
    * @return
    */
  def insert[K1 >: K, V1 >: V](k: K1, v: V1)(implicit ord: Ord[K1]): Map[K1, V1]

  /** Insert with a function, combining new value and old value. insertWith f key value mp
    * will insert the pair (key, value) into mp if key does not exist in the map. If the key
    * does exist, the function will insert the pair (key, f new_value old_value).
    * @param k
    * @param v
    * @param f
    * @param ord
    * @tparam K1
    * @tparam V1
    * @return
    */
  def insertWith[K1 >: K, V1 >: V](k: K1, v: V1)(f: (V1, V1) => V1)(implicit ord: Ord[K1]): Map[K1, V1]

  /** Delete a key and its value from the map. When the key is not a
    * member of the map, the original map is returned.
    * @param x
    * @param ord
    * @tparam K1
    * @return
    */
  def delete[K1 >: K](x: K1)(implicit ord: Ord[K1]): Map[K1, V]

  /** Update a value at a specific key with the result of the provided function. When the key is
    * not a member of the map, the original map is returned.
    * @param k
    * @param f
    * @param ord
    * @tparam K1
    * @tparam V1
    * @return
    */
  def adjust[K1 >: K, V1 >: V](k: K1)(f: V1 => V1)(implicit ord: Ord[K1]): Map[K1, V1]

  /** Return all keys of the map.
    * @return
    */
  def keys: List[K]

  /** Return all key/value pairs in the map.
    * @return
    */
  def entrySet: List[(K, V)]

  /** Map a function over all values in the map.
    * @param f
    * @tparam A
    * @return
    */
  def map[A](f: V => A): Map[K, A]

  /** Map values and collect the Just results.
    * @param f
    * @tparam A
    * @return
    */
  def mapMaybe[A](f: V => Maybe[A]): Map[K, A]

  /** Map values and collect the Good results.
    * @param f
    * @tparam A
    * @tparam B
    * @return
    */
  def mapOr[A, B](f: V => Either[A, B]): Map[K, A]
}

object Map {
  /** Create the empty map.
    * @tparam K
    * @tparam V
    * @return
    */
  def empty[K, V]: Map[K, V] = undefined

  /** Create a map with a single element.
    * @param kv
    * @tparam K
    * @tparam V
    * @return
    */
  def singleton[K, V](kv: (K, V)): Map[K, V] = undefined

  /** Create a map from key/value pairs.
    * @param kvPairs
    * @tparam K
    * @tparam V
    * @return
    */
  def apply[K, V](kvPairs: (K, V)*): Map[K, V] = undefined

  /** Create a map from a list of key/value pairs.
    * @param xs
    * @tparam K
    * @tparam V
    * @return
    */
  def fromList[K, V](xs: List[(K, V)]): Map[K, V] = undefined
}