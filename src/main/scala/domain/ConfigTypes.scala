package com.sourcelambda
package domain

import domain.RedisTypes.RedisUri

object ConfigTypes:
  
  case class RedisConfig(uri: RedisUri)
  
  case class AppConfig(redisConfig: RedisConfig)
 
end ConfigTypes
