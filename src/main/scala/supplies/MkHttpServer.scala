package com.sourcelambda
package supplies

import cats.effect.Resource
import cats.effect.Async
import org.http4s.server.Server
import org.http4s.ember.server.EmberServerBuilder
import com.comcast.ip4s.*
import org.http4s.HttpApp

trait MkHttpServer[F[_]]:

  def create(app: HttpApp[F]): Resource[F, Server]

end MkHttpServer


object MkHttpServer:
  def make[F[_] : Async]: MkHttpServer[F] = new MkHttpServer[F]:
      override def create(app: HttpApp[F]): Resource[F, Server] =
        EmberServerBuilder
          .default[F]
          .withHost(ipv4"0.0.0.0")
          .withPort(port"8080")
          .withHttpApp(app)
          .build
