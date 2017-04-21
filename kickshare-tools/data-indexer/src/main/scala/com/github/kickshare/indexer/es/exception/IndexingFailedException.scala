package com.github.kickshare.indexer.es.exception

/**
 * Throws when batch fails for any reason.
 * @param msg describing error.
 */
class IndexingFailedException(msg: String) extends Exception(msg)
