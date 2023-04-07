package dev.brave
package algebras

import scala.annotation.unused

import cats.MonadThrow
import cats.syntax.all.*

import skunk.*
import skunk.syntax.all.*

import dev.profunktor.redis4cats.RedisCommands

import domain.UserTypes.*
import domain.ItemTypes.*
import domain.CartTypes.*
import domain.SkunkTypes.Pool

import lib.typeclasses.GenUUID

trait ShoppingCart[F[_]]:

  def add(userId: UserId, itemId: ItemId, quantity: Int): F[Unit]
  def get(userId: UserId): F[List[CartItemSimple]]
  def delete(userId: UserId): F[Unit]
  def removeItem(userId: UserId, itemId: ItemId): F[Unit]

end ShoppingCart

object ShoppingCart:
  def make[F[_]: GenUUID : MonadThrow](
    redis: RedisCommands[F, String, String],
    items: Items[F],
    exp: ShoppingCartExpiration
  ): ShoppingCart[F] = new ShoppingCart[F]:
    
    def add(userId: UserId, itemId: ItemId, quantity: Quantity): F[Unit] =
      redis.hSet(userId.show, itemId.show, quantity.show) *>
        redis.expire(userId.show, exp).void

    def get(userId: UserId): F[List[CartItemSimple]] =
      redis.hGetAll(userId.show).flatMap {
        _.toList
          .traverse {
            case (k, v) =>
              for
                id <- GenUUID[F].get[ItemId](k)
                qt <- MonadThrow[F].catchNonFatal(v.toInt)
              yield CartItemSimple(id, qt)
          }
      }

    def delete(userId: UserId): F[Unit] =
      redis.del(userId.show).void

    def removeItem(userId: UserId, itemId: ItemId): F[Unit] =
      redis.hDel(userId.show, itemId.show).void

    @unused
    def update(userId: UserId, cart: Cart): F[Unit] =
      redis.hGetAll(userId.show).flatMap {
        _.toList.traverse_ {
          case (k, _) =>
            GenUUID[F].get[ItemId](k).flatMap { id =>
              cart.items.get(id).traverse_ { q =>
                redis.hSet(userId.show, k, q.show)
              }
            }
        } *> redis.expire(userId.show, exp).void
      }


  end make
  



