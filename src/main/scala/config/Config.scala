package com.sourcelambda
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
      env("SL_SC_REDIS_URI").default("host.docker.internal").as[String],
      env("SL_SC_DATABASE_URI").default("localhost").as[String],
      env("SL_SC_DATABASE_PORT").default("5432").as[Int],
      env("SL_SC_DATABASE_USERNAME").default("postgres").as[String],
      env("SL_SC_DATABASE_PASSWORD").default("postgres").as[String].secret,
    ).parMapN( (redisUri, dbUri, dbPort, dbUser, dbPass) =>
      
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
          uri = s"redis://$redisUri"
        )
      )
    ).load[F]
 
end Config
