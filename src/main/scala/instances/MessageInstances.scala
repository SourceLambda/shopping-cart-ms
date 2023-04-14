package com.sourcelambda
package instances

import io.circe.generic.semiauto.*
import io.circe.Codec
import domain.MessageTypes.Message

import cats.effect.Concurrent
import org.http4s.EntityEncoder
import org.http4s.circe.jsonEncoderOf

object MessageInstances:

  given Codec[Message] = deriveCodec
  given [F[_] : Concurrent]: EntityEncoder[F, Message] = jsonEncoderOf[F, Message]
  
end MessageInstances
