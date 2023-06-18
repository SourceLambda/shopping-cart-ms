package com.sourcelambda
package domain

import java.util.UUID

object CategoryTypes:

  type CategoryId = UUID
  type CategoryName = String
  
  case class Category(id: CategoryId, name: CategoryName)
 
end CategoryTypes
