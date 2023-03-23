package dev.brave
package app

import cats.syntax.all.*
import cats.effect.{ExitCode, IO, IOApp, Resource}
import org.typelevel.log4cats.Logger
import org.typelevel.log4cats.slf4j.Slf4jLogger
import dev.profunktor.redis4cats.log4cats.*
import com.comcast.ip4s.*
import org.http4s.HttpApp
import org.http4s.implicits.*
import org.http4s.ember.server.EmberServerBuilder
import natchez.Trace.Implicits.noop
import scala.concurrent.duration.*
import config.Config
import supplies.*
import algebras.{Items, ShoppingCart}
import http.HttpApi

object Main extends IOApp:
  
  given logger: Logger[IO] = Slf4jLogger.getLoggerFromName("Main")
    
  override def run(args: List[String]): IO[ExitCode] =
    Config
      .load[IO]
      .flatMap(cfg =>
        logger.info(s"Loading configurations: $cfg") >>
          AppResources.make[IO](cfg).use { resources =>

            val items         = Items.make(resources.postgres)
            val shoppingCart  = ShoppingCart.make(resources.redis, items, 300.seconds)
            val api           = HttpApi.make(shoppingCart)
            val server        = MkHttpServer.make[IO].create(api.httpApp)

            server.useForever
              .onCancel(logger.info("Closing server..."))
              .as(ExitCode.Success)

          }
      )

  end run
    
end Main