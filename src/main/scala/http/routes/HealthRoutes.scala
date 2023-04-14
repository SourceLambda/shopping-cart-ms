package com.sourcelambda
package http.routes

import cats.effect.Concurrent
import cats.syntax.all.*
import org.typelevel.log4cats.Logger
import org.http4s.HttpRoutes
import org.http4s.dsl.Http4sDsl
import org.http4s.server.Router
import domain.MessageTypes.Message
import instances.MessageInstances.given


final class HealthRoutes[F[_] : Concurrent : Logger] extends Http4sDsl[F]:

  private val prefixPath = "/"

  private def httpRoutes: HttpRoutes[F] =

    HttpRoutes.of[F] {
      case GET -> Root / "health" =>
        Logger[F].info("Health checked!") >>
          Ok("Safe and sound")

      case GET -> Root / "health" / message =>
        Ok(Message(s"$message from server!"))
    }

  val routes: HttpRoutes[F] = Router(prefixPath -> httpRoutes)
  
  
end HealthRoutes

