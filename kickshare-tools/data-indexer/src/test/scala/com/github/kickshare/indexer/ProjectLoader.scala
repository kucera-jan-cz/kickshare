package com.github.kickshare.indexer

import scala.io.Source

/**
  * @author Jan.Kucera
  * @since 21.4.2017
  */
object ProjectLoader {

  def load(): Iterator[Project] = {
    Source.fromResource("data/ks/Kickstarter_2017-04-15T22_21_18_122Z.json").getLines()
      .map(line => Project.parseJson(line))
  }
}
