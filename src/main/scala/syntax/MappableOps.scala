package dev.brave
package syntax

import lib.typeclasses.Mappable

object MappableOps:
  extension [T: Mappable] (mappable: T)
    def toMap: Map[String, String] = Mappable[T].toMap(mappable)
