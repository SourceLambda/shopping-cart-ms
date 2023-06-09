package com.sourcelambda
package domain

import domain.BrandTypes.*
import domain.CategoryTypes.*
import domain.CartTypes.*

import scala.util.control.NoStackTrace

object ItemTypes:
  
  type ItemId = String
  type ItemName = String
  type ItemDescription = String
  type Money = Float
  
  case class Item(
   id: ItemId,
   name: ItemName,
   description: ItemDescription,
   price: Money,
   brand: Brand,
   category: Category
  ):
    def cart(q: Quantity): CartItem =
      CartItem(this, q)
      
  end Item
  
  case class ItemNotFound(itemId: ItemId) extends NoStackTrace
 
end ItemTypes
