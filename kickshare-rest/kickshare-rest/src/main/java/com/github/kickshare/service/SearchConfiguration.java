package com.github.kickshare.service;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.concurrent.TimeUnit;

import org.elasticsearch.client.Client;
import org.elasticsearch.common.settings.Settings;
import org.elasticsearch.common.transport.InetSocketTransportAddress;
import org.elasticsearch.transport.client.PreBuiltTransportClient;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @author Jan.Kucera
 * @since 21.3.2017
 */

@Configuration
public class SearchConfiguration {

    @Bean
    public Client client() throws UnknownHostException {
        String host = "localhost";
        String clusterName = "elasticsearch";
        int port = 9300;

        Settings settings = Settings.builder()
                .put("cluster.name", clusterName)
                .put("client.transport.sniff", false)
                .put("client.transport.ping_timeout", 20, TimeUnit.SECONDS)
                .build();

        InetSocketTransportAddress address = new InetSocketTransportAddress(InetAddress.getByName(host), port);
        //@TODO - when elastic is down it kill whole application, consider running in semi-state?
        Client client = new PreBuiltTransportClient(settings).addTransportAddress(address);
        return client;
    }
}
