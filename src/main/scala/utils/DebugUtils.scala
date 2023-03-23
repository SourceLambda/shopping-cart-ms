package dev.brave
package utils

import cats.effect.Sync

import cats.syntax.functor.*
import cats.syntax.flatMap.*


extension [F[_] : Sync, A] (fa: F[A])
  def peek: F[A] =
    for
      a <- fa
      t <- Sync[F].delay(Thread.currentThread().getName)
      _ <- Sync[F].delay(println(s"[$t] $a"))
    yield a
