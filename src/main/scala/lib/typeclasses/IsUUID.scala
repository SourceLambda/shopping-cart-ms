package com.sourcelambda
package lib.typeclasses

import monocle.Iso

import java.util.UUID

trait IsUUID[A]:
  
  def iso: Iso[UUID, A]

object IsUUID:
  def apply[A : IsUUID]: IsUUID[A] = summon

  given IsUUID[UUID] with
    def iso: Iso[UUID, UUID] =
      Iso[UUID, UUID](identity)(identity)