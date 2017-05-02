package com.github.kickshare.indexer

import java.util.Locale

import com.github.javafaker.Faker
import com.github.kickshare.data.faker.Backer
import com.github.kickshare.indexer.common.slf4j.Slf4jLogging
import com.typesafe.scalalogging.Logger
import org.apache.commons.lang3.RandomUtils
import org.scalatest.FunSuite
import scalikejdbc.{ConnectionPool, ConnectionPoolSettings, DB, DBSession, SQL}

/**
  * @author Jan.Kucera
  * @since 16.4.2017
  */
class BackerIndexerTest extends FunSuite with Slf4jLogging {
  private val logger = Logger(classOf[IndexerTest])

  test("Load backers") {
    initConnection()

    for (schema <- Array("GB")) {
      val locale = Locale.getAvailableLocales.filter(l => schema.equalsIgnoreCase(l.getCountry)).take(1).apply(0)
      val faker = new Faker(locale)
      val cities = GeoCityLoader.load(schema).toList
      val projects = ProjectLoader.load().take(1000).toList


      DB localTx { implicit session =>
        SQL(s"""SET SCHEMA '${schema}'""").execute().apply()
        for (i <- 1 to 10) {
          logger.info("Creating leader: {}", i)
          createLeader(schema, faker, cities, projects)
        }

      }
    }
  }

  private def createLeader(schema: String, faker: Faker, cities: List[GeoCity], projects: List[Project])(implicit session: DBSession): Unit = {
    val numberOfProjects = RandomUtils.nextInt(1, 4)
    val leader = new Backer(faker.name().firstName(), faker.name().lastName(), faker.internet().emailAddress())
    val cityIndex = RandomUtils.nextInt(0, cities.length)

    val city = cities(cityIndex)
    val backersProjects = (for {i <- 0 to numberOfProjects} yield projects(RandomUtils.nextInt(0, projects.length))).toSeq

    val id: Long =
      SQL(s"""INSERT INTO backer (id, name, surname, email) VALUES (DEFAULT, ?, ?, ? ) RETURNING id""")
        .batchAndReturnGeneratedKey(Seq(leader.name, leader.surname, leader.email)).apply[Seq]().apply(0)
    SQL(s"""INSERT INTO backer_locations (backer_id, city_id, is_permanent_address) VALUES (?, ?, true)""")
      .batch(Seq(id, city.id))
    SQL(s"""INSERT INTO address (backer_id, street, city, postal_code) VALUES ({id}, {street}, {city}, {postal_code})""")
      .batchByName(Seq('id -> city.id, 'city -> city.name, 'street -> faker.address().streetAddress(), 'postal_code -> faker.address().zipCode()))

    val projectParams: Seq[Seq[Any]] = backersProjects.map(p => Seq(p.id.toLong, p.name, s"Description of ${p.name}", p.photoUrl))
    logger.info("Project params: {}", projectParams)
    val projectIds: Seq[Long] =
      SQL(s"""INSERT INTO project (id, name, description, url, deadline) VALUES (?, ?, ?, ?, CURRENT_DATE) ON CONFLICT DO NOTHING RETURNING id""")
        .batchAndReturnGeneratedKey(projectParams: _*).apply[Seq]()

    val groupParams = projectIds.zip(backersProjects).map(p => Seq('leader_id -> id, 'project_id -> p._1, 'name -> p._2.name,
      'lat -> city.latitude, 'lon -> city.longitude, 'is_local -> RandomUtils.nextBoolean()))
    SQL(
      s"""INSERT INTO "group" (leader_id, PROJECT_ID, name, lat, lon, is_local) VALUES
         |({leader_id}, {project_id}, {name}, {lat}, {lon}, {is_local})""".stripMargin)
      .batchByName(groupParams: _*).apply()
  }

  private def createBacker(schema: String, faker: Faker, cities: List[GeoCity], projects: List[Project])(implicit session: DBSession): Unit = {
    val backer = new Backer(faker.name().firstName(), faker.name().lastName(), faker.internet().emailAddress())
    val cityIndex = RandomUtils.nextInt(0, cities.length)
    val backedGroups = RandomUtils.nextInt(1, 9)
    val city = cities(cityIndex)

    val id: Long =
      SQL(s"""INSERT INTO backer (id, name, surname, email) VALUES (DEFAULT, ?, ?, ? ) RETURNING id""")
        .batchAndReturnGeneratedKey(Seq(backer.name, backer.surname, backer.email)).apply[Seq]().apply(0)
    SQL(s"""INSERT INTO backer_locations (backer_id, city_id, is_permanent_address) VALUES (?, ?, true)""")
      .batch(Seq(id, city.id))
    SQL(s"""INSERT INTO address (backer_id, street, city, postal_code) VALUES ({id}, {street}, {city}, {postal_code})""")
      .batchByName(Seq('id -> city.id, 'city -> city.name, 'street -> faker.address().streetAddress(), 'postal_code -> faker.address().zipCode()))

    SQL(s"""SELECT * FROM 'group' TABLESAMPLE BERNOULLI (10) LIMIT 2;"""
  }


  private def initConnection(): Unit = {
    val settings = ConnectionPoolSettings(
      initialSize = 10,
      maxSize = 10,
      connectionTimeoutMillis = 3000L,
      validationQuery = "SELECT 1",
      driverName = "org.postgresql.Driver")

    ConnectionPool.singleton("jdbc:postgresql://localhost:5432/kickshare", "kickshare", "kickshare", settings)
  }

  private def caseClassToSQL(cc: Product): Seq[(Symbol, Any)] = {
    val fieldNames = cc.getClass.getDeclaredFields.map(f => Symbol.apply(f.getName)) // all field names
    fieldNames.zip(cc.productIterator.to).toMap.toSeq
  }
}
