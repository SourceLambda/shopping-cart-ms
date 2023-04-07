package dev.brave
package instances

import cats.Show

import domain.CategoryTypes.Category

object CategoryInstances:
  
  given Show[Category] = Show.fromToString 
 
end CategoryInstances
