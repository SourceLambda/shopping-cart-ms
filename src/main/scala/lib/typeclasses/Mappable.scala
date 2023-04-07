package com.sourcelambda
package lib.typeclasses

trait Mappable[T]:
  
  def toMap(value: T): Map[String, String] 

end Mappable

object Mappable:
  def apply[T : Mappable]: Mappable[T] = summon