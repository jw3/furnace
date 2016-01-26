package com.rxthings.furnace

import akka.actor.{ActorRef, ActorSystem}
import com.rxthings.furnace.Events.{Register, Subscribe}
import com.rxthings.furnace.thermostats.DS18b20
import com.rxthings.inject._
import com.typesafe.config.Config
import com.typesafe.scalalogging.LazyLogging
import net.ceedubs.ficus.Ficus._
import rxgpio.pigpio.PigpioLibrary
import rxgpio.{DefaultInitializer, InitOK}

import scala.concurrent.Await
import scala.concurrent.duration.Duration
import scala.util.{Failure, Success}

object Boot extends App with LazyLogging {
    implicit val pigpio = BootUtils.rxgpio()
    implicit val sys = ActorSystem("FurnaceApp")

    val furnace = Furnace()
    val log: Option[ActorRef] = InjectActor[FurnaceLog]

    val config = sys.settings.config.getAs[Config]("furnace")
    val devices = config.flatMap(_.getAs[Seq[String]]("devices"))

    devices match {
        case Some(list) =>
            logger.debug(s"installing devices from config, $list")
            list.foreach(id => furnace ! Register(id, DS18b20(id)))
            log.foreach(l => list.foreach(id => l.tell(Subscribe(id), furnace)))

        case None =>
            logger.error("no devices specified in config")
            sys.terminate()
    }

    Await.ready(sys.whenTerminated, Duration.Inf)
}

object BootUtils extends LazyLogging {
    def rxgpio(): PigpioLibrary = {
        DefaultInitializer.gpioInitialise() match {
            // revisit;; non-exhaustive matching here, will address back in rxgpio
            case Success(InitOK(ver)) => logger.info(s"initialized pigpio:$ver")
            case Failure(e) => logger.error("failed to init gpio", e)
        }
        PigpioLibrary.Instance
    }
}


