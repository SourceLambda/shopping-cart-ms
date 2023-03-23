package dev.brave
package codecs

import skunk.*
import skunk.codec.all.*

import domain.BrandTypes.*
import domain.CategoryTypes.*
import domain.UserTypes.*
import domain.ItemTypes.*

object SkunkCodecs:

  val brandId: Codec[BrandId]     = uuid
  val brandName: Codec[BrandName] = varchar(50)

  val categoryId: Codec[CategoryId]     = uuid
  val categoryName: Codec[CategoryName] = varchar(50)

  val itemId: Codec[ItemId]            = uuid
  val itemName: Codec[ItemName]        = varchar(50)
  val itemDesc: Codec[ItemDescription] = text

  val userId: Codec[UserId] = uuid

  val money: Codec[Money] = numeric

end SkunkCodecs
