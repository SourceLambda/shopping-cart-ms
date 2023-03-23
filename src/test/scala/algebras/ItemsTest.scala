package dev.brave
package algebras

import scala.concurrent.duration.*
import cats.effect.{IO, IOApp}
import cats.syntax.all.*
import org.typelevel.log4cats.slf4j.Slf4jLogger
import org.typelevel.log4cats.Logger
import dev.profunktor.redis4cats.log4cats.*
import natchez.Trace.Implicits.noop
import config.Config
import domain.ConfigTypes.AppConfig
import supplies.{AppResources, MkPostgresSession}
import utils.peek

import dev.brave.domain.UserTypes.UserId
import dev.brave.lib.typeclasses.GenUUID

import java.util.UUID

object ItemsTest extends IOApp.Simple:
  
  given logger: Logger[IO] = Slf4jLogger.getLoggerFromName("Main")
  
  override def run: IO[Unit] = Config.load[IO].flatMap{ config =>
    logger.info(s"Loaded config: $config") >> AppResources.make[IO](config).use { resources =>
      
      val items = Items.make(resources.postgres)
      val sc = ShoppingCart.make(resources.redis, items, 300.seconds)
      
      val userId = UUID.fromString("193a42bf-2440-47a6-b452-b7e7afede97b")
      
      sc.add(userId, UUID.fromString("e3b69034-bb7c-4fa1-a584-f74625610c31"), 4) >>
        sc.get(userId).peek.void
      
    }
  }
    
end ItemsTest