package dev.brave
package domain

import cats.{Eq, Show}
import cats.derived.*
import lib.domain.IdNewType

import java.util.UUID

object UserTypes:

  type UserId = UUID
 
end UserTypes
