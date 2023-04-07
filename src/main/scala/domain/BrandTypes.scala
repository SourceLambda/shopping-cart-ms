package com.sourcelambda
package domain

import lib.domain.{IdNewType, NewType}

import java.util.UUID
import scala.util.control.NoStackTrace

object BrandTypes:

  type BrandId = UUID
  
  type BrandName = String
  
  case class Brand(id: BrandId, name: BrandName)

  case class InvalidBrand(value: String) extends NoStackTrace
 
end BrandTypes
