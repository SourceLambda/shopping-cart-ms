package dev.brave
package http.routes

import cats.Monad
import cats.syntax.all.*
import org.http4s.HttpRoutes
import org.http4s.dsl.Http4sDsl
import org.http4s.server.Router
import org.typelevel.log4cats.Logger

final class HealthRoutes[F[_] : Monad : Logger] extends Http4sDsl[F]:

  private val prefixPath = "/"

  private def httpRoutes: HttpRoutes[F] =

    HttpRoutes.of[F] {
      case GET -> Root / "health" =>
        Logger[F].info("Health checked!") >>
          Ok("Safe and sound")
    }

  val routes: HttpRoutes[F] = Router(prefixPath -> httpRoutes)
  
  
end HealthRoutes

