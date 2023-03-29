package dev.brave
package domain

import java.util.UUID
import scala.deriving.*
import cats.{Eq, Show}
import cats.instances.string.*
import squants.Money
import domain.BrandTypes.*
import domain.CategoryTypes.*
import domain.CartTypes.*

import scala.util.control.NoStackTrace

object ItemTypes:
  
  type ItemId = UUID
  type ItemName = String
  type ItemDescription = String
  type Money = BigDecimal
  
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
