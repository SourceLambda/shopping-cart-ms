package dev.brave
package domain

import ciris.Secret 
import domain.RedisTypes.RedisUri

object ConfigTypes:
  
  case class PostgresConfig(
     host: String,
     port: Int,
     user: String,
     password: Secret[String],
     max: Int
  )
  
  case class RedisConfig(uri: RedisUri)
  
  case class AppConfig(postgreSQL: PostgresConfig, redisConfig: RedisConfig)
 
end ConfigTypes
