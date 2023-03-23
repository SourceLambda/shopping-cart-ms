package dev.brave
package domain

import cats.effect.Resource

import skunk.Session

object SkunkTypes:

  type Pool[F[_]] = Resource[F, Session[F]]
  type SessionPool[F[_]] = Resource[F, Pool[F]]
 
end SkunkTypes
