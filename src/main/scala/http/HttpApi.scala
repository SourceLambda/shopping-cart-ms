package com.sourcelambda
package http

import algebras.ShoppingCart

import cats.effect.Async
import cats.syntax.semigroupk.*

import org.http4s.{HttpApp, HttpRoutes}
import org.http4s.server.middleware.CORS

import org.typelevel.log4cats.Logger

import com.sourcelambda.http.routes.{CartRoutes, HealthRoutes}
import com.sourcelambda.lib.typeclasses.GenUUID

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