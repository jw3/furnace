package com.rxthings.furnace.logs

import com.rxthings.furnace.Events.{Reading, ReadingFailure}
import com.rxthings.furnace.FurnaceLog
import com.sksamuel.elastic4s.ElasticClient
import com.sksamuel.elastic4s.ElasticDsl._
import com.sksamuel.elastic4s.jackson.ElasticJackson.Implicits._
import com.typesafe.scalalogging.LazyLogging


/**
 * Log to Elasticsearch
 */
class ElasticsearchLog extends FurnaceLog with LazyLogging {
    val elastic = ElasticClient.local

    def receive: Receive = {
        case reading: Reading => elastic.execute {
            index into "temperature" / "readings" source reading
        }
        case failure: ReadingFailure => elastic.execute {
            index into "temperature" / "failures" source failure
        }
    }
}
