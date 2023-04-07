package dev.brave
package instances

import cats.derived.*
import cats.{Eq, Show}
import cats.syntax.show.*

import io.circe.generic.semiauto
import io.circe.syntax.*
import io.circe.{Codec, Encoder}

import domain.ItemTypes.*
import domain.CartTypes.{CartItem, CartTotal, Quantity}

import lib.typeclasses.Mappable
import instances.{BrandInstances, CategoryInstances}

object ItemInstances:

  given Codec[Item]      = semiauto.deriveCodec
  given Codec[CartItem]  = semiauto.deriveCodec
  given Codec[CartTotal] = semiauto.deriveCodec

  given Show[Quantity] = Show.fromToString
  given Show[Item]     = Show.fromToString

  given Mappable[Item] with
    override def toMap(value: Item): Map[String, String] =

      // TODO: use a macro implementation instead
      import BrandInstances.given
      import CategoryInstances.given

      Map(
        "id"          -> value.id.show,
        "name"        -> value.name.show,
        "description" -> value.description.show,
        "price"       -> value.price.show,
        "brand"       -> value.brand.show,
        "category"    -> value.category.show
      )

end ItemInstances
