package dev.brave
package supplies

import cats.effect.std.Console
import cats.effect.{Temporal, Resource}
import cats.syntax.flatMap.*
import cats.syntax.option.*

import fs2.io.net.Network

import natchez.Trace

import org.typelevel.log4cats.Logger

import skunk.*
import skunk.codec.text.*
import skunk.implicits.*

import domain.ConfigTypes.*
import domain.SkunkTypes.{Pool, SessionPool}

trait MkPostgresSession[F[_]]:
  def create: SessionPool[F]


object MkPostgresSession:

  def make[F[_]: Temporal: Trace: Console: Network: Logger](
      config: PostgresConfig
  ): MkPostgresSession[F] =

    new MkPostgresSession[F]:

      private def checkPostgresConnection(postgres: Pool[F]): F[Unit] =
        postgres.use(se =>
          se
            .unique(sql"select version();".query(text))
            .flatMap(v => Logger[F].info(s"Connected to PostgreSQL version: $v"))
        )

      override def create: SessionPool[F] =
        Session
          .pooled(
            host = config.host,
            port = config.port,
            database = "sl_sc_db_test",
            user = config.user,
            password = config.password.value.some,
            max = 10,
          ).evalTap(checkPostgresConnection)