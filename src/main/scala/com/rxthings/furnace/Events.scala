package com.rxthings.furnace

import akka.actor.ActorRef
import scala.concurrent.duration.DurationInt
import scala.concurrent.duration.FiniteDuration

object Events {
    case class RequestReading()
    case class ReadingFailure(dev: String, t: Long, meta: Option[String] = None)

    trait Reading {
        def dev: String
        def c: Double
        def f: Double
        def t: Long
    }

    object Reading {
        def unapply(r: Reading) = Option((r.dev, r.c, r.f, r.t))
    }

    case class Subscribe(id: String)
    case class Refresh(id: String)
    case class Register(id: String, device: ActorRef)
    case class ScheduleUpdates(id: String, interval: FiniteDuration = 30 seconds)
}
