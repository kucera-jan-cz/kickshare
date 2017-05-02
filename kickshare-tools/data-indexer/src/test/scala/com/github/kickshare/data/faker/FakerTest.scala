package com.github.kickshare.data.faker

import java.util.Locale

import com.github.javafaker.Faker
import com.github.kickshare.indexer.Location
import com.github.kickshare.indexer.common.slf4j.Slf4jLogging
import com.typesafe.scalalogging.Logger
import org.apache.commons.lang3.RandomUtils
import org.scalatest.FunSuite

/**
  * @author Jan.Kucera
  * @since 31.3.2017
  */
class FakerTest extends FunSuite with Slf4jLogging {
  private val logger = Logger(classOf[FakerTest])

  test("create some data") {
    val faker = new Faker(new Locale("en-US"))
    for (i <- 1 to 100) yield {
      val user = User(faker.name().firstName(), faker.name().lastName(), faker.internet().safeEmailAddress(), location())
      logger.info("{}", user)
    }
  }

  def location(): Location = {
    val lat = RandomUtils.nextFloat(48, 52)
    val lon = RandomUtils.nextFloat(12, 20)
    Location(lat, lon)
  }

}
