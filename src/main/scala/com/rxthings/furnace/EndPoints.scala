package com.rxthings.furnace

import akka.http.scaladsl.server.Directives._
import wiii.awa.ActorWebApi

trait EndPoints {
    this: ActorWebApi =>

    val apiver = "v1"

    def setTempUpperBounds(v: Int)
    def setTempLowerBounds(v: Int)

    val setters =
        pathPrefix(apiver) {
            (put & path("sets")) {
                path("upper") {
                    complete("set-temp-upper-bounds")
                } ~
                path("lower") {
                    complete("set-temp-lower-bounds")
                }
            }
        }

    def getTempUpperBounds(): Int
    def getTempLowerBounds(): Int

    val getters =
        pathPrefix(apiver) {
            (get & path("gets")) {
                path("upper") {
                    complete("get-temp-upper-bounds")
                } ~
                path("lower") {
                    complete("get-temp-lower-bounds")
                }
            }
        }

    val controls =
        pathPrefix(apiver) {
            (put & path("control")) {
                path("run") {
                    path("for") {
                        path(IntNumber / Segment) { (num, t) =>
                            complete(s"run-for-$num-$t")
                        }
                    } ~
                    path("until") {
                        path("temp") {
                            complete("run-until-temp-at")
                        } ~
                            path("time") {
                                complete("run-until-time-of-day")
                            }
                    }
                }
            }
        }

    def statHighTemp(): Int
    def statLogTemp(): Int
    def statDailyAvg(): Int
    def statUptime(): Int

    val stats =
        pathPrefix(apiver) {
            (get & path("stats")) {
                path("high") {
                    complete("high-temp")
                } ~
                    path("low") {
                        complete("low-temp")
                    } ~
                    path("daily-avg") {
                        complete("daily-average-temp")
                    } ~
                    path("uptime") {
                        complete("system-uptime")
                    }
            }
        }
}
