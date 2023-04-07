package com.sourcelambda
package lib.domain

import lib.typeclasses.Wrapper.*
import lib.typeclasses.{IsUUID, Wrapper}

import cats.{Eq, Order, Show}
import io.circe.{Codec, Decoder, Encoder}
import monocle.Iso

import java.util.UUID

abstract class NewType[A](using
                          eqv: Eq[A],
                          ord: Order[A],
                          shw: Show[A],
                          enc: Encoder[A],
                          dec: Decoder[A]
//                          cod: Codec[A]
                         ):

  opaque type Type = A

  inline def apply(a: A): Type = a

  protected inline final def derive[F[_]](using ev: F[A]): F[Type] = ev

  extension (t: Type) inline def value: A = t

  given Wrapper[A, Type] with
    def iso: Iso[A, Type] =
      Iso[A, Type](apply(_))(_.value)

  given Eq[Type]       = eqv
  given Order[Type]    = ord
  given Show[Type]     = shw
  given Encoder[Type]  = enc
  given Decoder[Type]  = dec
//  given Codec[Type]    = cod
  given Ordering[Type] = ord.toOrdering


end NewType


abstract class IdNewType extends NewType[UUID]:
  given IsUUID[Type]                = derive[IsUUID]
  def unsafeFrom(str: String): Type = apply(UUID.fromString(str))