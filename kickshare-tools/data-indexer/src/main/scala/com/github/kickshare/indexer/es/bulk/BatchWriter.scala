package com.github.kickshare.indexer.es.bulk

import com.github.kickshare.indexer.es.exception.IndexingFailedException
import org.elasticsearch.action.index.IndexRequest

import scala.concurrent.duration.Duration

/**
  * Defines simplified API for writing IndexRequests as batch processing.
  */
trait BatchWriter {
  /**
    * Send given request to indexing pipeline.
    *
    * @param request indexing request which should be processed.
    */
  def send(request: IndexRequest): Unit

  /**
    * Waits given timeout to finishing indexing with 100% requests successful, otherwise throw exception.
    *
    * @param timeout duration defining maximum time for waiting to batch completion.
    * @throws InterruptedException      if the current thread is interrupted
    * @throws IndexingFailedException if at least one document has not been indexed
    */
  @throws[InterruptedException]
  @throws[IndexingFailedException]
  def awaitTermination(timeout: Duration): Unit

  /**
    * Waits given timeout to finishing indexing. Failures are allowed.
    *
    * @param timeout duration defining maximum time for waiting to batch completion.
    * @throws InterruptedException if the current thread is interrupted
    * @return number of failed documents
    */
  @throws[InterruptedException]
  def awaitCompletion(timeout: Duration): Long

}
