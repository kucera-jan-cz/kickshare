package com.github.kickshare.indexer.es.utils

import java.net.InetAddress
import java.util.concurrent.TimeUnit

import org.elasticsearch.client.Client
import org.elasticsearch.common.settings.Settings
import org.elasticsearch.common.transport.InetSocketTransportAddress
import org.elasticsearch.transport.client.PreBuiltTransportClient

object TransportClientFactory {
  def createClient(host: String = "localhost", clusterName: String = "elasticsearch", port: Int = 9300): Client = {
    val settings = Settings.builder()
      .put("cluster.name", clusterName)
      .put("client.transport.sniff", false)
      .put("client.transport.ping_timeout", 20, TimeUnit.SECONDS)
      .build()
    val address = new InetSocketTransportAddress(InetAddress.getByName(host), port)
    val client = new PreBuiltTransportClient(settings).addTransportAddress(address)
    client
  }
}
