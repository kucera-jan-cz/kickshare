package com.github.kickshare.indexer

import java.io.{BufferedReader, StringReader}
import java.util
import java.util.function.Consumer

import com.github.kickshare.indexer.common.slf4j.Slf4jLogging
import com.typesafe.scalalogging.Logger
import org.apache.http.entity.ContentType
import org.apache.http.nio.entity.NStringEntity
import org.apache.http.{HttpEntity, HttpHost}
import org.elasticsearch.client.RestClient
import org.json4s.JsonDSL._
import org.json4s.jackson.JsonMethods._
import org.scalatest.FunSuite

import scala.io.Source

/**
  * @author Jan.Kucera
  * @since 16.3.2017
  */

class IndexerTest extends FunSuite with Slf4jLogging {
  private val logger = Logger(classOf[IndexerTest])

  ignore("load all CZ cities") {
    val client = RestClient.builder(new HttpHost("localhost", 9200)).build()
    val params = new util.HashMap[String, String]()
    val consumer: Consumer[HttpEntity] = (entity: HttpEntity) => {
      client.performRequest("POST", "/cities/data", params, entity)
    }

    Source.fromResource("data/geo/cities_cz.txt").getLines()
      .take(1000)
      .map(processLine)
      .map(new NStringEntity(_, ContentType.APPLICATION_JSON))
      .foreach(consumer.accept(_))

  }

  test("load all postal codes") {
    val postalCount = Source.fromResource("data/geo/postal_codes_cz.txt").getLines()
        .map(_.split('\t'))
        .filter(p => "CZ".equals(p(0)))
        .size

    val cityCount =  Source.fromResource("data/geo/cities_cz.txt").getLines()
      .map(_.split('\t'))
      .filter(p => "CZ".equals(p(8)))
      .size
    logger.info("{}{}", "Postal vs Cities", Array(postalCount, cityCount))
  }

  private def processLine(line: String): String = {
    val parts = line.split('\t')
    val location = ("lat" -> parts(4).toFloat) ~ ("lon" -> parts(5).toFloat)
    val json = ("name" -> parts(1)) ~ ("id" -> parts(0)) ~ ("location" -> location)
    compact(json)
  }

  private def loadJson(): BufferedReader = {
    val json = Source.fromInputStream(getClass.getResourceAsStream("/data/geo/cities_cz.txt")).mkString
    val reader = new BufferedReader(new StringReader(json))
    reader
  }
}
