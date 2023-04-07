package com.sourcelambda
package instances

import cats.Show

import domain.BrandTypes.Brand

object BrandInstances:
  
  given Show[Brand] = Show.fromToString
 
end BrandInstances
