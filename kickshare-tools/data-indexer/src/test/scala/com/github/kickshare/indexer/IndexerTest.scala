package com.github.kickshare.indexer

import java.io.{BufferedReader, StringReader}
import java.util
import java.util.concurrent.TimeUnit
import java.util.function.Consumer

import com.github.kickshare.indexer.common.slf4j.Slf4jLogging
import com.github.kickshare.indexer.es.bulk.BatchDefinition
import com.github.kickshare.indexer.es.utils.TransportClientFactory
import com.typesafe.scalalogging.Logger
import org.apache.http.{HttpEntity, HttpHost}
import org.elasticsearch.action.index.IndexRequest
import org.elasticsearch.client.RestClient
import org.json4s.JsonDSL._
import org.json4s.jackson.JsonMethods._
import org.scalatest.FunSuite

import scala.concurrent.duration.Duration
import scala.io.Source

/**
  * @author Jan.Kucera
  * @since 16.3.2017
  */

class IndexerTest extends FunSuite with Slf4jLogging {
  private val logger = Logger(classOf[IndexerTest])
  private val CITY_CODES = List("PPL", "PPLA", "PPLC")

  test("load all CZ cities") {
    val client = TransportClientFactory.createClient()
    val restClient = RestClient.builder(new HttpHost("localhost", 9200)).build()
    val params = new util.HashMap[String, String]()
    val consumer: Consumer[HttpEntity] = (entity: HttpEntity) => {
      restClient.performRequest("POST", "/cities/data", params, entity)
    }

    val batch = new BatchDefinition()(client)
    GeoCityLoader.load("CZ")
      .map(processLine)
      .map(new IndexRequest("cities_raw", "data").source(_))
      .foreach(batch.send(_))
    //      .map(new NStringEntity(_, ContentType.APPLICATION_JSON))
    //      .foreach(consumer.accept(_))
    batch.awaitTermination(Duration(10, TimeUnit.MINUTES))
    client.close()
    restClient.close()
  }

  ignore("load all postal codes") {
    val postalCount = Source.fromResource("data/geo/postal_codes_cz.txt").getLines()
      .map(_.split('\t'))
      .filter(p => "CZ".equals(p(0)))
      .size

    val cityCount = Source.fromResource("data/geo/cities_cz.txt").getLines()
      .map(_.split('\t'))
      .filter(p => "CZ".equals(p(8)))
      .size
    logger.info("{}{}", "Postal vs Cities", Array(postalCount, cityCount))
  }

  private def processLine(city: GeoCity): String = {
    val location = ("lat" -> city.latitude) ~ ("lon" -> city.longitude)
    val json = ("name" -> city.name) ~ ("id" -> city.id) ~ ("location" -> location) ~ ("raw" -> city.toString) ~ ("feature_class" -> city.featureClass) ~ ("feature_code" -> city.featureCode) ~ ("csv" -> city.csv)
    compact(json)
  }

  private def processLine(parts: Array[String]): String = {
    val location = ("lat" -> parts(4).toFloat) ~ ("lon" -> parts(5).toFloat)
    val json = ("name" -> parts(1)) ~ ("id" -> parts(0)) ~ ("location" -> location) ~ ("raw" -> parts.mkString("|"))
    compact(json)
  }

  private def loadJson(): BufferedReader = {
    val json = Source.fromInputStream(getClass.getResourceAsStream("/data/geo/cities_cz.txt")).mkString
    val reader = new BufferedReader(new StringReader(json))
    reader
  }
}
