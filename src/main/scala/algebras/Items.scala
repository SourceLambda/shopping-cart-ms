package dev.brave
package algebras

import cats.syntax.all.*

import skunk.*
import skunk.syntax.all.*

import domain.ItemTypes.*
import domain.CategoryTypes.Category
import domain.BrandTypes.Brand
import domain.SkunkTypes.Pool

import cats.effect.MonadCancelThrow

trait Items[F[_]]:

  def findById(itemId: ItemId): F[Option[Item]]
  def add(item: Item): F[ItemId]
  
end Items

object Items:
  def make[F[_] : MonadCancelThrow](postgres: Pool[F]): Items[F] = new Items[F]:
    
    import ItemsSQL.*

    override def findById(itemId: ItemId): F[Option[Item]] =
      postgres.use ( se =>
        se.prepare(selectById).flatMap( ps =>
          ps.option(itemId)
        )
      )

    override def add(item: Item): F[ItemId] =
      postgres.use ( se =>
        se.prepare(addItem).flatMap( cmd =>
          cmd
            .execute(item)
            .as(item.id)
            .recoverWith {case id => id.raiseError[F, ItemId]}
        )
      )
      

private object ItemsSQL:
  
  import dev.brave.codecs.SkunkCodecs.*

  val decoder: Decoder[Item] =
    (itemId ~ itemName ~ itemDesc ~ money ~ brandId ~ brandName ~ categoryId ~ categoryName).map {
      case i ~ n ~ d ~ p ~ bi ~ bn ~ ci ~ cn =>
        Item(i, n, d, p, Brand(bi, bn), Category(ci, cn))
    }
    
  val encoder: Encoder[Item] =
    (itemId ~ itemName ~ itemDesc ~ money ~ brandId ~ brandName ~ categoryId ~ categoryName)
      .contramap( i =>
          i.id ~ i.name ~ i.description ~ i.price ~ i.brand.id ~ i.brand.name ~ i.category.id ~ i.category.name)

  val selectById: Query[ItemId, Item] =
    sql"""
          SELECT i.id, i.name, i.description, i.price, i.brand_id, i.brand_name, i.category_id, i.category_name
          FROM items AS i
          WHERE i.id = $itemId
         """.query(decoder)
    
  val addItem: Command[Item] =
    sql"""
         INSERT INTO items
         VALUES ($itemId, $itemName, $itemDesc, $money, $brandId, $brandName, $categoryId, $categoryName)
       """.command.contramap {
            case i =>
              i.id ~ i.name ~ i.description ~ i.price ~ i.brand.id ~ i.brand.name ~ i.category.id ~ i.category.name
          }