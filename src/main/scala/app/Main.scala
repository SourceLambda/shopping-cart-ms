package com.sourcelambda
package app

import cats.effect.{ExitCode, IO, IOApp}
import org.typelevel.log4cats.Logger
import org.typelevel.log4cats.slf4j.Slf4jLogger
import dev.profunktor.redis4cats.log4cats.*
import scala.concurrent.duration.*
import config.Config
import supplies.*
import algebras.ShoppingCart
import http.HttpApi

object Main extends IOApp:
  
  given logger: Logger[IO] = Slf4jLogger.getLoggerFromName("App")
    
  override def run(args: List[String]): IO[ExitCode] =
    Config
      .load[IO]
      .flatMap(cfg =>
        logger.info(s"Loading configurations: $cfg") >>
          AppResources.make[IO](cfg).use: resources =>

//            val items         = Items.make(resources.postgres)
            val shoppingCart  = ShoppingCart.make(resources.redis, 2.hours)
            val api           = HttpApi.make(shoppingCart)
            val server        = MkHttpServer.make[IO].create(api.httpApp)
              .evalTap( server =>
                IO(server).onError( e => logger.error(e)("Error ocurred creating server: \n"))
              )
            
            server
              .useForever
              .onCancel(logger.info("Closing server..."))
              .as(ExitCode.Success)
          
      )

  end run
    
end Main