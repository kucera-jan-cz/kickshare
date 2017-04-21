package com.github.kickshare.indexer.es.scroll

import org.elasticsearch.action.search.SearchResponse

/**
 * API for reading/scrolling documents from index.
 */
trait ScrollSearch {
  /**
   * Provides iterator with paged results.
   * @return Iterator of SearchResponses representing Scroll-based results
   */
  def getResponses(): Iterator[SearchResponse]
}
