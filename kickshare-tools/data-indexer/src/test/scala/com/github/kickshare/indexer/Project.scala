package com.github.kickshare.indexer

import com.fasterxml.jackson.databind.ObjectMapper
import com.typesafe.scalalogging.Logger
import org.json4s.JsonDSL._
import org.json4s.jackson.JsonMethods.compact

/**
  * @author Jan.Kucera
  * @since 2.4.2017
  */
case class Project(id: String, name: String, url: String, photoUrl: String) {
  def toJson(): String = {
    val json = ("name" -> name) ~ ("photo" -> photoUrl) ~ ("campaign_id" -> id)
    compact(json)
  }
}

//id,photo,name,blurb,goal,pledged,state,slug,disable_communication,country,currency,currency_symbol,currency_trailing_code,deadline,state_changed_at,created_at,launched_at,staff_pick,backers_count,static_usd_rate,usd_pledged,creator,location,category,profile,spotlight,urls,source_url,friends,is_starred,is_backing,permissions
//"733476420","{""small"":""https://ksr-ugc.imgix.net/assets/012/098/920/5c1b77436fe763bb22b6dc6b3343eb3e_original.jpg?w=160&h=90&fit=fill&bg=000000&v=1463737216&auto=format&q=92&s=6b0bf4ee5c11450a45aed9e43d024e21"",""thumb"":""https://ksr-ugc.imgix.net/assets/012/098/920/5c1b77436fe763bb22b6dc6b3343eb3e_original.jpg?w=40&h=22&fit=fill&bg=000000&v=1463737216&auto=format&q=92&s=7d8a9748cbe76712eea42e0d06ff08bf"",""1024x576"":""https://ksr-ugc.imgix.net/assets/012/098/920/5c1b77436fe763bb22b6dc6b3343eb3e_original.jpg?w=1024&h=576&fit=fill&bg=000000&v=1463737216&auto=format&q=92&s=93fd2290d07eb666e9275f22d3dac9ad"",""med"":""https://ksr-ugc.imgix.net/assets/012/098/920/5c1b77436fe763bb22b6dc6b3343eb3e_original.jpg?w=266&h=150&fit=fill&bg=000000&v=1463737216&auto=format&q=92&s=9d7851d38cf875edba8a77ea9d21e3b9"",""key"":""assets/012/098/920/5c1b77436fe763bb22b6dc6b3343eb3e_original.jpg"",""1536x864"":""https://ksr-ugc.imgix.net/assets/012/098/920/5c1b77436fe763bb22b6dc6b3343eb3e_original.jpg?w=1536&h=864&fit=fill&bg=000000&v=1463737216&auto=format&q=92&s=5f8bcb0bb2fcff3b0121f1f91a235004"",""ed"":""https://ksr-ugc.imgix.net/assets/012/098/920/5c1b77436fe763bb22b6dc6b3343eb3e_original.jpg?w=338&h=190&fit=fill&bg=000000&v=1463737216&auto=format&q=92&s=f9c156f86699606e384c98a032906039"",""full"":""https://ksr-ugc.imgix.net/assets/012/098/920/5c1b77436fe763bb22b6dc6b3343eb3e_original.jpg?w=560&h=315&fit=fill&bg=000000&v=1463737216&auto=format&q=92&s=cdf00b3fa24803c52b3cfe00317def6b"",""little"":""https://ksr-ugc.imgix.net/assets/012/098/920/5c1b77436fe763bb22b6dc6b3343eb3e_original.jpg?w=200&h=112&fit=fill&bg=000000&v=1463737216&auto=format&q=92&s=72b8626fe9dc69e17802ed45ce7905b6""}","Burnin' 4 You Woodburning","I have found a passion for creating art from woodburning. I would like to share my talent with everyone else and allow them to enjoy it","1250","80","failed","burnin-4-you-woodburning","false","US","USD","$","true","1431038917","1431038921","1428347626","1428446917","false","4","1","80.0","{""urls"":{""web"":{""user"":""https://www.kickstarter.com/profile/1785411136""},""api"":{""user"":""https://api.kickstarter.com/v1/users/1785411136?signature=1484618339.b2a217079a3c8e69657a3fa34386fb7129674f5d""}},""name"":""Todd McLelland"",""id"":1785411136,""avatar"":{""small"":""https://ksr-ugc.imgix.net/assets/009/623/548/8eedf99c17d1c57c9072f092faefc0bf_original.jpeg?w=80&h=80&fit=crop&v=1461607818&auto=format&q=92&s=35f2cb629c3419a6f3caf15aff963936"",""thumb"":""https://ksr-ugc.imgix.net/assets/009/623/548/8eedf99c17d1c57c9072f092faefc0bf_original.jpeg?w=40&h=40&fit=crop&v=1461607818&auto=format&q=92&s=729b998bf6ec649fe9817ca8153e81ed"",""medium"":""https://ksr-ugc.imgix.net/assets/009/623/548/8eedf99c17d1c57c9072f092faefc0bf_original.jpeg?w=160&h=160&fit=crop&v=1461607818&auto=format&q=92&s=e57bf3648b65bd5e13333accbc9f57ee""}}","{""country"":""US"",""urls"":{""web"":{""discover"":""https://www.kickstarter.com/discover/places/ogden-ut"",""location"":""https://www.kickstarter.com/locations/ogden-ut""},""api"":{""nearby_projects"":""https://api.kickstarter.com/v1/discover?signature=1484610579.8c2ddad083dd76a3bac1fb73d707d2f224360ab0&woe_id=2464337""}},""name"":""Ogden"",""displayable_name"":""Ogden, UT"",""short_name"":""Ogden, UT"",""id"":2464337,""state"":""UT"",""type"":""Town"",""is_root"":false,""slug"":""ogden-ut""}","{""urls"":{""web"":{""discover"":""http://www.kickstarter.com/discover/categories/art/illustration""}},""color"":16760235,""parent_id"":1,""name"":""Illustration"",""id"":22,""position"":4,""slug"":""art/illustration""}","{""background_image_opacity"":0.8,""should_show_feature_image_section"":true,""link_text_color"":null,""state_changed_at"":1428347626,""blurb"":null,""background_color"":null,""project_id"":1824828,""name"":null,""feature_image_attributes"":{""image_urls"":{""default"":""https://ksr-ugc.imgix.net/assets/012/098/920/5c1b77436fe763bb22b6dc6b3343eb3e_original.jpg?w=1536&h=864&fit=fill&bg=000000&v=1463737216&auto=format&q=92&s=5f8bcb0bb2fcff3b0121f1f91a235004"",""baseball_card"":""https://ksr-ugc.imgix.net/assets/012/098/920/5c1b77436fe763bb22b6dc6b3343eb3e_original.jpg?w=1536&h=864&fit=fill&bg=000000&v=1463737216&auto=format&q=92&s=5f8bcb0bb2fcff3b0121f1f91a235004""}},""link_url"":null,""show_feature_image"":false,""id"":1824828,""state"":""inactive"",""text_color"":null,""link_text"":null,""link_background_color"":null}","false","{""web"":{""project"":""https://www.kickstarter.com/projects/1785411136/burnin-4-you-woodburning?ref=category"",""rewards"":""https://www.kickstarter.com/projects/1785411136/burnin-4-you-woodburning/rewards""}}","https://www.kickstarter.com/discover/categories/art/illustration?ref=category_modal&sort=magic",,,,
object Project {
  private val logger = Logger(classOf[Project])
  private val mapper = new ObjectMapper()

  def parse(l: String): Project = {
    try {
//      val formatted = l.replaceAll("\"\"", "'").replaceAll("\"", "")
      val p = l.split("\",\"")
      val id = p(0).replaceAll("\"", "")
      val name = p(10).replaceAll("\"", "")
      val photo = p(1).replaceAll("\"", "").replace("{small:", "")
      logger.info("{}", p(2).replaceAll("\"", ""))
      Project(id, name, null, photo)
    } catch {
      case ex: ArrayIndexOutOfBoundsException => {
        print(ex)
        null
      }

    }
  }

  def parseJson(l: String): Project = {
    val root = mapper.readTree(l)
    val id = root.get("id").longValue()
    val data = root.get("data")
    val name = data.get("name").textValue()
    val url = data.get("urls").get("web").get("project").textValue()
    val photoUrl = data.get("photo").get("small").textValue()
    Project(id.toString, name, url, photoUrl)
  }

}
