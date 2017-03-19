package com.github.kickshare.indexer.common.slf4j

import org.slf4j.Logger

trait Slf4jLogging {
  implicit class Implicits(logger: Logger) {
    def trace(format: String, arguments: Any*): Unit = {
      logger.trace(format, arguments.map(_.asInstanceOf[AnyRef]): _*)
    }

    def debug(format: String, arguments: Any*): Unit = {
      logger.debug(format, arguments.map(_.asInstanceOf[AnyRef]): _*)
    }

    def info(format: String, arguments: Any*): Unit = {
      logger.info(format, arguments.map(_.asInstanceOf[AnyRef]): _*)
    }

    def warn(format: String, arguments: Any*): Unit = {
      logger.warn(format, arguments.map(_.asInstanceOf[AnyRef]): _*)
    }

    def error(format: String, arguments: Any*): Unit = {
      logger.error(format, arguments.map(_.asInstanceOf[AnyRef]): _*)
    }
  }

}
