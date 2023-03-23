package dev.brave
package instances

import cats.derived.*
import cats.{Eq, Show}
import domain.ItemTypes.*

import dev.brave.domain.CartTypes.{CartItem, CartTotal, Quantity}
import io.circe.generic.semiauto
import io.circe.{Codec, Encoder}

object ItemInstances:

  given Codec[Item] = semiauto.deriveCodec
  given Codec[CartItem] = semiauto.deriveCodec
  given Codec[CartTotal] = semiauto.deriveCodec
  
  given Show[Quantity] = Show.fromToString
 
end ItemInstances
