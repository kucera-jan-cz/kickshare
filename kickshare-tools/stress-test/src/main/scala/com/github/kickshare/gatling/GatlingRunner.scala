package com.github.kickshare.gatling

import io.gatling.app.Gatling
import io.gatling.core.config.GatlingPropertiesBuilder

/**
  * @author Jan.Kucera
  * @since 4.5.2017
  */
object GatlingRunner {
  def main(args: Array[String]) {


    // This sets the class for the simulation we want to run.
    val simClass = classOf[BasicItSimulation].getName

    val props = new GatlingPropertiesBuilder
//    props.binariesDirectory("./target")
    props.sourcesDirectory("./src/main/scala/com/github/kickshare/gatling")
    props.simulationClass(simClass)
    props.outputDirectoryBaseName("./target")
    Gatling.fromMap(props.build)
  }
}
