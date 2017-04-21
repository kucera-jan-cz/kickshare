package com.github.kickshare.indexer.es.bulk

import java.util.concurrent.atomic.AtomicLong

import com.github.kickshare.indexer.common.slf4j.Slf4jLogging
import org.elasticsearch.action.bulk.BulkProcessor.Listener
import org.elasticsearch.action.bulk.{BulkRequest, BulkResponse}
import org.slf4j.LoggerFactory

/**
  * Elasticsearch listener with additional logging and calculation of failed requests.
  */
class LoggingBulkListener extends Listener with Slf4jLogging {
  private val logger = LoggerFactory.getLogger(classOf[LoggingBulkListener])
  private val failedDocs = new AtomicLong(0L)

  def beforeBulk(executionId: Long, request: BulkRequest): Unit = {
    logger.debug("Bulk initialized")
  }

  def afterBulk(executionId: Long, request: BulkRequest, response: BulkResponse): Unit = {
    if (response.hasFailures()) {

      val numOfFailures = response.getItems.count(_.isFailed)
      logger.warn("Bulk finished with {} failures", numOfFailures)
      if(logger.isTraceEnabled()) {
        logger.warn("Reason:\n{}", response.buildFailureMessage())
      }
      failedDocs.addAndGet(numOfFailures)
    } else {
      logger.debug("Bulk of {} docs finished. Took: {} ms", request.numberOfActions(), response.getTookInMillis())
    }
  }

  def afterBulk(executionId: Long, request: BulkRequest, failure: Throwable): Unit = {
    logger.warn("Bulk failed with exception: ", failure)
    failedDocs.addAndGet(request.requests().size())
  }

  /**
   * @return number of failed documents.
   */
  def numOfFailures() : Long = {
    failedDocs.get()
  }
}
