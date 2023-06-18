package com.sourcelambda
package supplies

import scala.concurrent.duration.*
import scala.io.AnsiColor.*

import cats.syntax.all.*
import cats.effect.{Resource, Temporal}
import cats.effect.std.Console

import dev.profunktor.redis4cats.*
import dev.profunktor.redis4cats.effect.MkRedis

import fs2.io.net.Network

import org.typelevel.log4cats.Logger

import domain.ConfigTypes.RedisConfig



trait MkRedisCommands[F[_]]:
  
  def create: Resource[F, RedisCommands[F, String, String]]
 
end MkRedisCommands

object MkRedisCommands:
  def make[F[_]: Temporal: Console: Logger: MkRedis: Network]
  (config: RedisConfig): MkRedisCommands[F] = new MkRedisCommands[F]:
    
    private def checkRedisConnection(
      redis: RedisCommands[F, String, String]
    ): F[Unit] =
      redis.info.flatMap:
        _.get("redis_version").traverse_ : v =>
          Logger[F].info(s"Connected to Redis $v")


    private def retryConnection(attempts: Int): Resource[F, RedisCommands[F, String, String]] =
      Redis[F].utf8(config.uri).attempt.flatMap:
        case Right(redis) => Resource.pure(redis)
        
        case Left(_) if attempts =!= 0 =>
          Resource.eval(
            Logger[F].error(s"${RED}Can't connect to Redis server, trying again...${RESET}") >>
              Temporal[F].sleep(5.seconds)
          ) >> retryConnection(attempts - 1)
          
        case Left(err) if attempts === 0 => Resource.eval:
          Logger[F].error(err):
            "Can't connect to Redis server after 5 tries, shutting down application."
          >> Temporal[F].raiseError(err)
        
    end retryConnection
    
    
    override def create: Resource[F, RedisCommands[F, String, String]] =
      retryConnection(5).evalTap(checkRedisConnection)
  

end MkRedisCommands
