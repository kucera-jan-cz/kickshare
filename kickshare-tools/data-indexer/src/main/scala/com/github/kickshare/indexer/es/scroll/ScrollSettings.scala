package com.github.kickshare.indexer.es.scroll

import com.github.kickshare.indexer.common.slf4j.Slf4jLogging
import org.elasticsearch.action.search.{SearchRequestBuilder, SearchResponse, SearchType}
import org.elasticsearch.client.Client
import org.elasticsearch.common.unit.TimeValue
import org.elasticsearch.index.query.{QueryBuilder, QueryBuilders}
import org.elasticsearch.search.sort.SortBuilder
import org.slf4j.LoggerFactory

/**
 * Perform scroll-search and provides results as Iterator[SearchResponse].
 * @param srcIndex
 * @param srcType
 * @param query
 * @param sorts
 * @param scrollSize how many docs should be retrieved in one scroll
 * @param scrollTime ES scroll timeout time between two scroll requests
 * @param client
 */
class ScrollSettings(
  val srcIndex: String,
  val srcType: Array[String] = Array.empty,
  val query: QueryBuilder = QueryBuilders.matchAllQuery(),
  val sorts: Array[SortBuilder[_]] = Array.empty,
  val scrollSize: Int = 10000,
  val scrollTime: TimeValue = new TimeValue(600000))
                    (implicit client: Client) extends ScrollSearch with Slf4jLogging {
  val logger = LoggerFactory.getLogger(classOf[ScrollSettings])


  /**
   * @inheritdoc
   */
  override def getResponses(): Iterator[SearchResponse] = {
    new ResponseIterator(client, this)
  }

  /**
   * @inheritdoc
   */
  private def getQuery(): SearchRequestBuilder = {
    // Setup the query
    val queryBuilder = client.prepareSearch(srcIndex).setTypes(srcType: _*)
      .setSearchType(SearchType.DEFAULT)
      .setQuery(query)
      .setScroll(scrollTime)
      .setSize(scrollSize)
    sorts.foreach(queryBuilder.addSort(_))
    queryBuilder
  }

  /**
   * Iterator returning SearchResponse with Scroll search under the covers.
   * @param client ES client for performing scroll
   * @param searchSettings providing actual search query and scroll related parameters.
   */
  private class ResponseIterator(client: Client, searchSettings: ScrollSettings) extends Iterator[SearchResponse] {
    private var scrollResp: SearchResponse = null
    private var scrollFuture = searchSettings.getQuery().execute()
    private var processed = 0L
    private var startTime = System.currentTimeMillis()
    def next: SearchResponse = {
      scrollResp
    }

    def hasNext: Boolean = {
      scrollResp = scrollFuture.get()
      processed += scrollResp.getHits.hits().length
      val duration = System.currentTimeMillis() - startTime
      val docStatus = s"[${processed}/${scrollResp.getHits.totalHits()}]"
      val timesStatus = s"[Elastic: ${scrollResp.getTook.getMillis} Real:${duration}]"
      logger.debug("Scroll of {} {} took {} ms", searchSettings.scrollSize, docStatus, timesStatus)
      if (scrollResp.getHits.hits().length > 0) {
        scrollFuture = client.prepareSearchScroll(scrollResp.getScrollId).setScroll(searchSettings.scrollTime).execute()
        startTime = System.currentTimeMillis()
        true
      } else {
        false
      }
    }
  }

}
