package com.github.kickshare.indexer

import com.github.kickshare.indexer.common.slf4j.Slf4jLogging
import com.typesafe.scalalogging.Logger
import org.apache.commons.lang3.text.WordUtils
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

  test("Load cities") {
    GeoCityLoader.load("CZ").take(10).foreach(logger.info("{}", _))
  }

  test("Load categories") {
    val escapeSQL = (c: Category) => {
      c.copy(name = c.name.replaceAll("'", "''"), slug = c.slug.replaceAll("'", "''"))
    }
    val categories = CategoryLoader.load().toList.distinct.map(escapeSQL).sortBy(_.id)
    val rootCategories = categories.map(cat => {
      val slug = cat.slug.split('/')(0)
      val name = WordUtils.capitalize(slug)
      Category(cat.parentId, name, -1L, slug)
    }).distinct.sortBy(_.id)
    val root2value = (c: Category) => s"""(${c.id}, '${c.name}', FALSE, -1, '${c.slug}')"""
    val insertSQL = "INSERT INTO category (id, name, is_root, parent_id, slug) VALUES\n"
    val rootSQL = insertSQL + rootCategories.map(root2value).mkString(",\n") + ";"
    logger.info("Root SQL: \n{}", rootSQL)
    val cat2value = (c: Category) => s"""(${c.id}, '${c.name}', TRUE, ${c.parentId}, '${c.slug}')"""
    val catSQL = insertSQL + categories.map(cat2value).mkString(",\n") + ";"
    logger.info("Category SQL: \n{}", catSQL)
  }
}
