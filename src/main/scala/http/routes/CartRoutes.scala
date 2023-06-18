package com.sourcelambda
package http.routes

import cats.syntax.all.*

import org.typelevel.log4cats.Logger

import org.http4s.HttpRoutes
import org.http4s.dsl.Http4sDsl
import org.http4s.server.Router
import org.http4s.circe.*
import org.http4s.EntityDecoder
import org.http4s.circe.CirceEntityEncoder.circeEntityEncoder
import org.http4s.circe.CirceEntityDecoder.circeEntityDecoder

import io.circe.generic.auto.*

import algebras.ShoppingCart
import domain.CartTypes.*
import domain.UserTypes.UserId
import instances.ItemInstances.given
import lib.typeclasses.GenUUID

import cats.effect.Concurrent

class CartRoutes[F[_] : GenUUID : Concurrent : Logger](shoppingCart: ShoppingCart[F]) extends Http4sDsl[F]:
  
  private val prefixPath = "/cart"
  
  private given EntityDecoder[F, CartItemSimple] = jsonOf[F, CartItemSimple]
  
  private def httpRoutes: HttpRoutes[F] =

    HttpRoutes.of[F] {
      
      // Retrieves the current user's cart
      case GET -> Root / userId =>
        GenUUID[F].get[UserId](userId).flatMap( userId =>
          Ok(shoppingCart.get(userId))
        ).onError(e => Logger[F].error(e)("Error in routes:"))
        
      // Adds an item to a user's cart
      case ar @ POST -> Root / userId =>
        ar.as[CartItemSimple].flatMap( cartItem =>
          GenUUID[F].get[UserId](userId).flatMap( userId =>
            shoppingCart.add(userId, cartItem.itemId, cartItem.quantity) >>
              Logger[F].info(s"Added item: ${cartItem.itemId}") >>
               Created(shoppingCart.get(userId))
          )
        ).onError(e => Logger[F].error(e)("Error in routes:"))
        
      // Deletes an item from a user's cart
      case ar @ PATCH -> Root / userId =>
        ar.as[RemoveFromCart].flatMap( remove =>
          GenUUID[F].get[UserId](userId).flatMap( userId =>
            shoppingCart.removeItem(userId, remove.itemId ) >>
              Logger[F].info(s"Removed item: ${remove.itemId}") >>
                NoContent()
          )
        ).onError(e => Logger[F].error(e)("Error in routes:"))
        
      // Delete the cart from a user
      case DELETE -> Root / userId =>
        GenUUID[F].get[UserId](userId).flatMap( userId =>
          shoppingCart.delete(userId) >> ResetContent()
        )
    }

  val routes: HttpRoutes[F] = Router(prefixPath -> httpRoutes)
  
end CartRoutes
