package com.github.kickshare.indexer

import com.fasterxml.jackson.databind.ObjectMapper

/**
  * @author Jan.Kucera
  * @since 24.4.2017
  */
case class Category(id: Long, name: String, parentId: Long, slug: String) {
}

object Category {
  private val mapper = new ObjectMapper()

  def parseJson(l: String): Category = {
    val root = mapper.readTree(l)
    val categoryNode = root.path("data").path("category")
    val id = categoryNode.path("id").asLong(-1L)
    val name = categoryNode.path("name").asText("")
    val parentId = categoryNode.path("parent_id").asLong(-1L)
    val slug = categoryNode.path("slug").asText("")
    Category(id, name, parentId, slug)
  }
}
