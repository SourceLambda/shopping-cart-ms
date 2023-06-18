package com.sourcelambda
package lib.typeclasses

import java.util.UUID

import cats.effect.Sync
import cats.syntax.functor.*

trait GenUUID[F[_]]:
  def make[A : IsUUID]: F[A]
  def get[A : IsUUID](value: String): F[A]

object GenUUID:
  def apply[F[_] : GenUUID]: GenUUID[F] = summon

  given[F[_] : Sync]: GenUUID[F] with
    override def make[A: IsUUID]: F[A] =
      Sync[F].delay(UUID.randomUUID()).map(IsUUID[A].iso.get)

    override def get[A: IsUUID](value: String): F[A] =
      Sync[F].catchNonFatal(UUID.fromString(value)).map(IsUUID[A].iso.get)

