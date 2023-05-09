package com.sourcelambda
package supplies

import cats.syntax.all.*
import cats.effect.{Resource, Temporal}
import cats.effect.std.Console
import org.typelevel.log4cats.Logger

import dev.profunktor.redis4cats.*
import dev.profunktor.redis4cats.effect.MkRedis

import com.sourcelambda.domain.ConfigTypes.AppConfig
import fs2.io.net.Network

case class AppResources[F[_]] (redis: RedisCommands[F, String, String])

object AppResources:
  def make[F[_] : Temporal : MkRedis : Console : Network : Logger]
    (config: AppConfig): Resource[F, AppResources[F]] =
      (
        MkRedisCommands.make[F](config.redisConfig).create
      ).map(AppResources.apply)
  