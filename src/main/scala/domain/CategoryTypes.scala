package dev.brave
package domain

import dev.brave.lib.domain.{IdNewType, NewType}

import java.util.UUID

object CategoryTypes:

  type CategoryId = UUID
  type CategoryName = String
  
  case class Category(id: CategoryId, name: CategoryName)
 
end CategoryTypes
