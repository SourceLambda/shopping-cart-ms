package dev.brave
package http

import algebras.{Items, ShoppingCart}

import cats.Monad
import cats.effect.Async
import cats.effect.kernel.Concurrent
import cats.syntax.semigroupk.*
import org.http4s.{HttpApp, HttpRoutes}
import org.http4s.server.middleware.CORS
import dev.brave.http.routes.{CartRoutes, HealthRoutes}
import dev.brave.lib.typeclasses.GenUUID
import dev.profunktor.redis4cats.RedisCommands
import org.typelevel.log4cats.Logger

abstract class HttpApi[F[_] : Async : GenUUID : Logger](private val shoppingCart: ShoppingCart[F]):
  
  private val healthRoutes = HealthRoutes[F].routes
  
  private val cartRoutes = CartRoutes[F](shoppingCart).routes
  
  private val allRoutes    = healthRoutes <+> cartRoutes

  private def middleware(http: HttpRoutes[F]): HttpRoutes[F] =
   CORS.policy.withAllowOriginAll(http)

  val httpApp: HttpApp[F] = middleware(allRoutes).orNotFound

end HttpApi


object HttpApi:
  def make[F[_] : Async : GenUUID : Logger](shoppingCart: ShoppingCart[F]): HttpApi[F] =
    new HttpApi[F](shoppingCart) {}