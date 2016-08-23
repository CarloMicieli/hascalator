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
package dst

import Prelude._

/**
  * It represents a binary search tree.
  *
  * A binary tree is:
  *  - either an empty node;
  *  - or a node contains 3 parts, a value, two children which are also trees.
  *
  * @tparam K the `Key` type
  * @tparam V the `Value` type
  *
  * @author Carlo Micieli
  * @since 0.0.1
  */
trait Tree[+K, +V] extends Any {

  /**
    * `O(1)` Return the root element for this `Tree`.
    * @return the root element
    */
  def get: (K, V)

  /**
    * `O(n)` The depth from this `Tree`.
    * @return the tree depth
    */
  def depth: Int

  /**
    * `O(h)` The element with the maximum key in this `Tree` according to the key ordering.
    * @return `Just(max)` if this `Tree` is not empty, `None` otherwise
    */
  def max: Maybe[K]

  /**
    * `O(h)` The element with the minimum key in this `Tree` according to the key ordering.
    * @return `Just(min)` if this `Tree` is not empty, `None` otherwise
    */
  def min: Maybe[K]

  /**
    * `O(n)` Returns the number of pair key-value contained in this `Tree`.
    * @return the number elements in the tree
    */
  def size: Int

  /**
    * `O(1)` Checks whether the current `Tree` is empty.
    * @return `true` if this tree is empty; `false` otherwise
    */
  def isEmpty: Boolean

  /**
    * `O(h)` It searches for the `key` in this `Tree`. It returns `Just(value)` when the
    * `key` is found, `None` otherwise.
    *
    * @usecase def lookup(key: K): Maybe[V]
    * @inheritdoc
    * @param key the key to find
    * @param ord the key ordering
    * @tparam K1
    * @return optionally the value associated with `key`
    */
  def lookup[K1 >: K](key: K1)(implicit ord: Ord[K1]): Maybe[V]

  /**
    * `O(h)` If the key `key` doesn't exist, this operation will insert it with the value `value`. On the
    * other hand, if the `key` already exists the value is updated applying the function `f` to the
    * current value.
    *
    * @usecase def upsert(key: K, value: V)(f: V => V): Tree[K, V]
    * @inheritdoc
    * @param key the key
    * @param value the value assigned to `key`, if not already in the tree
    * @param f the function applied to the already present value
    * @param ord
    * @tparam K1
    * @tparam V1
    * @return
    */
  def upsert[K1 >: K, V1 >: V](key: K1, value: V1)(f: V1 => V1)(implicit ord: Ord[K1]): Tree[K1, V1]

  /**
    * `O(h)` Checks whether this `Tree` contains the provided key.
    *
    * @usecase def contains(key: K): Boolean
    * @inheritdoc
    * @param key the key to search
    * @param ord the key ordering
    * @tparam K1 the key type
    * @return `true` if the key is in `Tree`; `false` otherwise
    */
  def contains[K1 >: K](key: K1)(implicit ord: Ord[K1]): Boolean

  /**
    * `O(h)` Inserts the `key` and the corresponding `value` to the `Tree`. If the `key` already exists
    * this operation will replace the previous value.
    *
    * @usecase def insert(key: K, value: V): Tree[K, V]
    * @inheritdoc
    * @param key the new element key
    * @param value the new element value
    * @param ord the key ordering
    * @tparam K1 the key type
    * @tparam V1 the value type
    * @return a new `Tree`, with the addition of the new element
    */
  def insert[K1 >: K, V1 >: V](key: K1, value: V1)(implicit ord: Ord[K1]): Tree[K1, V1]

  /**
    * `O(h)` Inserts this key and value pair to the `Tree`. If the `key` already exists
    * this operation will replace the previous value.
    *
    * @usecase def insert(keyValuePair: (K, V)): Tree[K, V]
    * @inheritdoc
    * @param keyValuePair the key and value pair
    * @param ord the key ordering
    * @tparam K1 the key type
    * @tparam V1 the value type
    * @return a new `Tree`
    */
  def insert[K1 >: K, V1 >: V](keyValuePair: (K1, V1))(implicit ord: Ord[K1]): Tree[K1, V1] = {
    insert(keyValuePair._1, keyValuePair._2)
  }

  /**
    * `O(h)` Delete the node with the provided key. If this `Tree` doesn't contain the key, the
    * `Tree` is returned unchanged.
    *
    * @usecase def delete(key: K): (Maybe[V], Tree[K, V])
    * @inheritdoc
    * @param key the key to be removed
    * @param ord the key ordering
    * @tparam K1
    * @return a pair of the removed element and the modified `Tree`
    */
  def delete[K1 >: K](key: K1)(implicit ord: Ord[K1]): (Maybe[V], Tree[K1, V])

  /**
    * `O(n)` Apply the function `f` to the values in this `Tree`.
    * @param f the function to be applied
    * @tparam V1 the new value type
    * @return a new tree
    */
  def map[V1](f: V => V1): Tree[K, V1]

  /**
    * `O(n)` It folds the current tree values using the provided function.
    *
    * @usecase def fold(f: (V, V) => V): V
    * @inheritdoc
    * @param f
    * @tparam V1
    * @return
    */
  def fold[V1 >: V](f: (V1, V1) => V1): V1

  /**
    * `O(n)` Convert this `Tree` to a `List` of pair.
    * @return the list with the pair `(key, value)` in this `Tree`
    */
  def toList: List[(K, V)]
}