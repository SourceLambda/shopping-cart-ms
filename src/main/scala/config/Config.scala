package com.sourcelambda
package config

import cats.effect.Async
import ciris.*
import domain.ConfigTypes.*

object Config:
  
  def load[F[_] : Async]: F[AppConfig] =
    (
      env("SL_SC_REDIS_URI").default("host.docker.internal").as[String],
    ).map( redisUri =>
      
      AppConfig(
        
        // Redis
        RedisConfig(
          uri = s"redis://$redisUri"
        )
      )
    ).load[F]
 
end Config
