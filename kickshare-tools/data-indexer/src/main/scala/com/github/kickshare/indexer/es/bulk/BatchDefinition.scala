package com.github.kickshare.indexer.es.bulk

import java.util.concurrent.TimeUnit

import com.github.kickshare.indexer.es.exception.IndexingFailedException
import org.elasticsearch.action.bulk.BulkProcessor.Listener
import org.elasticsearch.action.bulk.{BackoffPolicy, BulkProcessor}
import org.elasticsearch.action.index.IndexRequest
import org.elasticsearch.client.Client
import org.elasticsearch.common.unit.{ByteSizeUnit, ByteSizeValue, TimeValue}

import scala.concurrent.duration.Duration

class BatchDefinition(
                       bulkItems: Int = 1000, bulkSizeMB: Int = 50, threads: Int = 1,
                       retryInterval: Int = 100, maxRetries: Int = 3)
                     (implicit client: Client) extends BatchWriter {
  private val loggingListener = new LoggingBulkListener()
  private lazy val bulkProcessor = prepareBulk(loggingListener)

  def send(request: IndexRequest): Unit = {
    bulkProcessor.add(request)
  }

  def awaitTermination(timeout: Duration): Unit = {
    val failures = awaitCompletion(timeout)
    if (failures > 0) {
      throw new IndexingFailedException(s"Couldn't index $failures docs")
    }
  }

  def awaitCompletion(timeout: Duration): Long = {
    bulkProcessor.awaitClose(timeout.toMillis, TimeUnit.MILLISECONDS)
    val failures = loggingListener.numOfFailures()
    failures
  }

  private def prepareBulk(listener: Listener): BulkProcessor = {
    val bulkProcessor = BulkProcessor.builder(client, listener)
      .setBulkActions(bulkItems)
      .setBulkSize(new ByteSizeValue(bulkSizeMB, ByteSizeUnit.MB))
      .setConcurrentRequests(threads)
      .setBackoffPolicy(
        BackoffPolicy.exponentialBackoff(TimeValue.timeValueMillis(retryInterval), maxRetries)
      )
      .build()
    bulkProcessor
  }
}
