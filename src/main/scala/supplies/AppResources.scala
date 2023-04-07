package com.sourcelambda
package supplies

import cats.syntax.all.*
import cats.effect.{Resource, Temporal}
import cats.effect.std.Console
import org.typelevel.log4cats.Logger

import dev.profunktor.redis4cats.*
import dev.profunktor.redis4cats.effect.MkRedis

import domain.SkunkTypes.Pool

import com.sourcelambda.domain.ConfigTypes.AppConfig
import fs2.io.net.Network
import natchez.Trace


case class AppResources[F[_]] (
                                postgres: Pool[F],
                                redis: RedisCommands[F, String, String]
                              )

object AppResources:
  def make[F[_] : Temporal : MkRedis : Trace : Console : Network : Logger]
    (config: AppConfig): Resource[F, AppResources[F]] =
      (
        MkPostgresSession.make[F](config.postgreSQL).create,
        MkRedisCommands.make[F](config.redisConfig).create
      ).mapN(AppResources.apply)
  