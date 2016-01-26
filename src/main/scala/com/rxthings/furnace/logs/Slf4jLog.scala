package com.rxthings.furnace.logs

import com.rxthings.furnace.FurnaceLog
import com.typesafe.scalalogging.LazyLogging

/**
 * Forwarder to Slf4j
 */
class Slf4jLog extends FurnaceLog with LazyLogging {
    def receive: Receive = {
        case m => logger.info(s"furnace-log [$m]")
    }
}
