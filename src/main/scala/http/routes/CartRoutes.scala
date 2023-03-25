package dev.brave
package http.routes

import cats.Monad
import cats.syntax.all.*

import org.typelevel.log4cats.Logger

import org.http4s.{EntityEncoder, HttpRoutes}
import org.http4s.dsl.Http4sDsl
import org.http4s.server.Router
import org.http4s.circe.*
import org.http4s.EntityDecoder
import org.http4s.circe.CirceEntityEncoder.circeEntityEncoder
import org.http4s.circe.CirceEntityDecoder.circeEntityDecoder

import io.circe.syntax.*
import io.circe.generic.auto.*

import java.util.UUID

import algebras.ShoppingCart
import domain.CartTypes.{Cart, CartTotal, ComingCart, RemoveFromCart}
import domain.UserTypes.UserId
import instances.ItemInstances.given
import lib.typeclasses.GenUUID

import cats.effect.Concurrent
class CartRoutes[F[_] : GenUUID : Concurrent : Logger](shoppingCart: ShoppingCart[F]) extends Http4sDsl[F]:
  
  private val prefixPath = "/cart"
  
  private given EntityDecoder[F, ComingCart] = jsonOf[F, ComingCart]
  
  private def httpRoutes: HttpRoutes[F] =

    HttpRoutes.of[F] {
      
      // Retrieves the current user's cart
      case GET -> Root / userId =>
        GenUUID[F].get[UserId](userId).flatMap( userId =>
          Logger[F].info(s"The ID is: $userId") >>
          Ok(shoppingCart.get(userId))
        )
        
      // Adds an item to a user's cart
      case ar @ POST -> Root / userId =>
        ar.as[ComingCart].flatMap( tup =>
          GenUUID[F].get[UserId](userId).flatMap( userId =>
            shoppingCart.add(userId, tup.itemId, tup.quantity) >>
              Logger[F].info(s"Added item: ${tup.itemId}") >>
               Created()
          )
        )
        
      // Deletes an item from a user's cart
      case ar @ PATCH -> Root / userId =>
        ar.as[RemoveFromCart].flatMap( remove =>
          GenUUID[F].get[UserId](userId).flatMap( userId =>
            shoppingCart.removeItem(userId, remove.itemId ) >> NoContent()
          )
        )
    }

  val routes: HttpRoutes[F] = Router(prefixPath -> httpRoutes)
  
end CartRoutes
