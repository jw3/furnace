package com.rxthings.furnace


import akka.actor._
import com.rxthings.furnace.Events._
import com.typesafe.scalalogging.LazyLogging
import wiii.awa.ActorWebApi

import scala.annotation.meta
import scala.collection.mutable
import scala.concurrent.duration.{DurationInt, FiniteDuration}


object Furnace {
    def props() = Props[Furnace]
    def apply()(implicit sys: ActorSystem) = sys.actorOf(props())
}

class Furnace extends Actor with ActorWebApi with LazyLogging {
    val devices = mutable.Map[String, ActorRef]()
    val subscribers = new mutable.HashMap[String, mutable.Set[ActorRef]] with mutable.MultiMap[String, ActorRef]

    val cancellers = mutable.Map[String, Cancellable]()

    def receive: Receive = {
        case m @ Register(id, device) =>
            logger.info(s"register $m")
            devices(id) = device

        case m @ Subscribe(id) =>
            logger.info(s"$m")
            subscribers.addBinding(id, sender())

        case m @ Refresh(id) if devices.contains(id) =>
            logger.info(s"$m")
            devices(id) ! RequestReading()

        case r: Reading =>
            subscribers.get(r.dev).foreach(_.foreach(_ ! r))

        case f @ ReadingFailure(id, t, meta) =>
            logger.warn(s"failed to read DS18b20: id:[$id] meta:[${meta.getOrElse("")}]")
            subscribers.get(f.dev).foreach(_.foreach(_ ! f))

        case m @ ScheduleUpdates(id, interval) =>
            cancellers(id) = scheduleUpdate(id, interval)

        case m @ _ =>
            logger.warn(s"unhandled message [$m]")
    }

    def scheduleUpdate(id: String, interval: FiniteDuration) =
        context.system.scheduler.schedule(5 seconds, interval, self, Refresh(id))(context.system.dispatcher)

    override def preStart(): Unit = {
        logger.trace(s"starting Furnace")
    }
}
