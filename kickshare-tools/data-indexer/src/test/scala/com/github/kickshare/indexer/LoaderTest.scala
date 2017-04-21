package com.github.kickshare.indexer

import com.github.kickshare.indexer.common.slf4j.Slf4jLogging
import com.typesafe.scalalogging.Logger
import org.scalatest.FunSuite

/**
  * @author Jan.Kucera
  * @since 16.3.2017
  */

class LoaderTest extends FunSuite with Slf4jLogging {
  private val logger = Logger(classOf[LoaderTest])

  test("load projects") {
    ProjectLoader.load().take(5).foreach(logger.info("{}", _))
  }
}
