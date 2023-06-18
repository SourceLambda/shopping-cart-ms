package com.sourcelambda
package domain

import io.circe.{Encoder, Decoder}
import scala.util.control.NoStackTrace
import domain.ItemTypes.*
import domain.UserTypes.*

import scala.concurrent.duration.FiniteDuration

object CartTypes:
  type Quantity = Int
  type ShoppingCartExpiration = FiniteDuration

  case class Cart(items: Map[ItemId, Quantity])
  

  object Cart:
    given jsonEncoder: Encoder[Cart] =
      Encoder.forProduct1("items")(_.items)

    given jsonDecoder: Decoder[Cart] =
      Decoder.forProduct1("items")(Cart.apply)
      
      
  case class CartItemSimple(itemId: ItemId, quantity: Quantity)
  
  case class RemoveFromCart(itemId: ItemId)
  
  case class CartItem(item: Item, quantity: Quantity):
    def subTotal: Money = item.price * quantity
    
  case class CartTotal(items: List[CartItemSimple])
  case class CartNotFound(userId: UserId) extends NoStackTrace
 
end CartTypes
