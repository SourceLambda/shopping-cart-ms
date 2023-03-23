package dev.brave
package supplies

import cats.effect.{Resource, Concurrent}
import cats.effect.std.Console
import cats.syntax.all.*

import dev.profunktor.redis4cats.*
import dev.profunktor.redis4cats.effect.MkRedis

import fs2.io.net.Network

import org.typelevel.log4cats.Logger

import domain.ConfigTypes.RedisConfig



trait MkRedisCommands[F[_]]:
  
  def create: Resource[F, RedisCommands[F, String, String]]
 
end MkRedisCommands

object MkRedisCommands:
  def make[F[_]: Concurrent: Console: Logger: MkRedis: Network]
  (config: RedisConfig): MkRedisCommands[F] = new MkRedisCommands[F]:
    
    private def checkRedisConnection(
                                      redis: RedisCommands[F, String, String]
                                    ): F[Unit] =
      redis.info.flatMap {
        _.get("redis_version").traverse_ { v =>
          Logger[F].info(s"Connected to Redis $v")
        }
      }
    
    override def create: Resource[F, RedisCommands[F, String, String]] =
      Redis[F].utf8(config.uri).evalTap(checkRedisConnection)
      
    

