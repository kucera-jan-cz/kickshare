package com.github.kickshare.indexer

import scala.io.Source

/**
  * @author Jan.Kucera
  * @since 16.4.2017
  */
object GeoCityLoader {
  private val CITY_CODES = List("PPL", "PPLA", "PPLC")

  def load(countryCode: String): Iterator[GeoCity] = {
    val suffix = countryCode.toLowerCase()
    val countryFilter = (city: GeoCity) => countryCode.equals(city.countryCode)
    val cityFilter = filterOnlyCities(_)
    val cities = Source.fromResource(s"data/geo/cities_${suffix}.txt").getLines()
      .map(GeoCity.parse(_))
      .filter(countryFilter)
      .filter(cityFilter)
    cities
  }

  private def filterOnlyCities(city: GeoCity): Boolean = {
    (
      //Includes cities and district towns
      ("P".equals(city.featureClass) && CITY_CODES.contains(city.featureCode))
        ||
        //For including capital cities
        ("A".equals(city.featureClass) && "ADM1".equals(city.featureCode))
      )
  }
}
