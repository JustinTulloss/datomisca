/*
 * Copyright 2012 Pellucid and Zenexity
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package datomisca

import scala.language.reflectiveCalls


object Fact {
  /** Creates a single Add operation targeting a given [[DId]]
    *
    * In Clojure, this is equivalent to:
    * {{{[:db/add entity-id attribute value]}}}
    *
    * {{{
    * val totoName = Datomic.Fact.add(DId(Partition.USER))( person / "name" -> "toto")
    * }}}
    *
    * @param id the targeted [[DId]]
    * @param prop a tuple ([[Keyword]], value)<br/>
    *             where value can be any String/Long/Double/Float/Boolean/Date/BigInt/BigDec/DRef
    *             converted to [[DatomicData]] using [[toDWrapper]] implicit conversion
    */
  def add[T, U](id: T)(prop: (Keyword, U))(implicit ev0: AsEntityId[T], ev1: ToDatomicCast[U]) =
    new AddFact(ev0.conv(id), prop._1, ev1.to(prop._2))

  /** Creates a single Retract operation targeting a given [[DId]]
    *
    * In Clojure, this is equivalent to:
    * {{{[:db/retract entity-id attribute value]}}}
    *
    * {{{
    * val totoName = Datomic.Fact.retract(DLong(3L))( person / "name" -> "toto")
    * }}}
    *
    * @param id the targeted [[DId]]
    * @param prop a tuple ([[Keyword]], value)<br/>
    *             where value can be any String/Long/Double/Float/Boolean/Date/BigInt/BigDec/DRef
    *             converted to [[DatomicData]] using [[toDWrapper]] implicit conversion
    */
  def retract[T, U](id: T)(prop: (Keyword, U))(implicit ev0: AsPermanentEntityId[T], ev1: ToDatomicCast[U]) =
    new RetractFact(ev0.conv(id), prop._1, ev1.to(prop._2))

  /** Helper: creates a special AddToEntity for creating a new Partition
    *
    * {{{
    * val addPartOp = Datomic.addPartition(Partition(Namespace.DB.PART / "mypart"))
    * }}}
    *
    * @param partition the partition to create
    */
  def partition(partition: Partition) =
    new AddEntity(DId(Partition.DB), Map(
      Namespace.DB / "ident"              -> partition.keyword,
      Namespace.DB.INSTALL / "_partition" -> Partition.DB.keyword
    ))

}
