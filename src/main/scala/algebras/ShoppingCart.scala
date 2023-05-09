package com.sourcelambda
package algebras

import scala.annotation.unused
import cats.{Applicative, MonadThrow}
import cats.syntax.all.*
import dev.profunktor.redis4cats.RedisCommands
import domain.UserTypes.*
import domain.ItemTypes.*
import domain.CartTypes.*
import lib.typeclasses.GenUUID

trait ShoppingCart[F[_]]:

  def add(userId: UserId, itemId: ItemId, quantity: Int): F[Unit]
  def get(userId: UserId): F[CartTotal]
  def delete(userId: UserId): F[Unit]
  def removeItem(userId: UserId, itemId: ItemId): F[Unit]

end ShoppingCart

object ShoppingCart:
  def make[F[_] : MonadThrow](
    redis: RedisCommands[F, String, String],
    exp: ShoppingCartExpiration
  ): ShoppingCart[F] = new ShoppingCart[F]:
    
    def add(userId: UserId, itemId: ItemId, quantity: Quantity): F[Unit] =
      redis.hSet(userId.show, itemId.show, quantity.show) *>
        redis.expire(userId.show, exp).void

    def get(userId: UserId): F[CartTotal] =
      redis.hGetAll(userId.show).flatMap {
        _.toList
          .traverse {
            case (k, v) =>
              for
                id <- Applicative[F].pure(k)
                qt <- MonadThrow[F].catchNonFatal(v.toInt)
              yield CartItemSimple(id, qt)
          }
          .map(CartTotal.apply)
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
            Applicative[F].pure(k).flatMap { id =>
              cart.items.get(id).traverse_ { q =>
                redis.hSet(userId.show, k, q.show)
              }
            }
        } *> redis.expire(userId.show, exp).void
      }
    
  end make
  



