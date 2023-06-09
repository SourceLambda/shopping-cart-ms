package com.sourcelambda
package instances

import domain.CartTypes.CartItemSimple

import io.circe.{Codec, Decoder, Encoder}
import io.circe.generic.semiauto

object CartInstances:
  
  given Codec[CartItemSimple] = semiauto.deriveCodec
 
end CartInstances
