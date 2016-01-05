package com.rxthings.furnace.thermostats

import akka.actor.{Actor, ActorSystem, Props}
import com.rxthings.furnace.Events.{Reading, ReadingFailure, RequestReading}
import com.typesafe.scalalogging.LazyLogging

import scala.io.Source

class DS18b20(id: String, source: Source) extends Actor with LazyLogging {
    import DS18b20._

    def receive: Receive = {
        case RequestReading() =>
            sender() ! readDevice(source).map(success(_)).getOrElse(failure())
    }

    def success(v: Int) = DS18b20Reading(id, v)
    def failure() = ReadingFailure(id, System.currentTimeMillis())
}

object DS18b20 {
    val regex = """t=(\d+)""".r.unanchored
    def props(id: String) = Props(classOf[DS18b20], id, Source.fromFile(s"/sys/bus/w1/devices/$id/w1_slave"))
    def apply(id: String)(implicit sys: ActorSystem) = sys.actorOf(props(id))

    case class DS18b20Reading(dev: String, v: Int) extends Reading {
        val t: Long = System.currentTimeMillis()
        lazy val c: Double = v / 1000
        lazy val f: Double = c * 1.8 + 32
    }

    def readDevice(source: Source): Option[Int] = {
        val lines = source.getLines().toSeq
        if (lines.head.endsWith("YES")) lines.last match {
            case regex(v) => Option(v.toInt)
            case _ => None
        }
        else {
            None
        }
    }
}
