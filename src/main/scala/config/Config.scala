package dev.brave
package config

import cats.effect.Async
import cats.syntax.all.*

import java.nio.file.Path
import com.comcast.ip4s.*
import ciris.*
import domain.ConfigTypes.*



object Config:

  def load[F[_] : Async]: F[AppConfig] =
    (
      env("SL_SC_DATABASE_URI").default("localhost").as[String],
      env("SL_SC_DATABASE_PORT").default("5432").as[Int],
      env("SL_SC_DATABASE_USERNAME").default("postgres").as[String],
      env("SL_SC_DATABASE_PASSWORD").default("postgres").as[String].secret,
    ).parMapN( (dbUri, dbPort, dbUser, dbPass) =>
      
      AppConfig(
        
        // PostgreSQL
        PostgresConfig(
          host = dbUri,
          port = dbPort,
          user = dbUser,
          password = dbPass,
          max = 10
        ),
        
        // Redis
        RedisConfig(
          uri = "redis://localhost"
        )
      )
    ).load[F]
 
end Config