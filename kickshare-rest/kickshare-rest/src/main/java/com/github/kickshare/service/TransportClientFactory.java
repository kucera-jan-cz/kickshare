package com.github.kickshare.service;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.concurrent.TimeUnit;
import java.util.function.Supplier;

import org.elasticsearch.client.Client;
import org.elasticsearch.common.settings.Settings;
import org.elasticsearch.common.transport.InetSocketTransportAddress;
import org.elasticsearch.common.transport.TransportAddress;
import org.elasticsearch.transport.client.PreBuiltTransportClient;

/**
 * @author Jan.Kucera
 * @since 3.4.2017
 */
public class TransportClientFactory implements Supplier<Client> {
    private String clusterName;
    private String host;
    private int port;

    public TransportClientFactory() {
        this("localhost", "elasticsearch", 9300);
    }

    public TransportClientFactory(final String host, final String clusterName, final int port) {
        this.clusterName = clusterName;
        this.host = host;
        this.port = port;
    }

    public Client createClient() {
        Settings settings = Settings.builder()
                .put("cluster.name", clusterName)
                .put("client.transport.sniff", false)
                .put("client.transport.ping_timeout", 20, TimeUnit.SECONDS)
                .build();
        final TransportAddress address;
        try {
            address = new InetSocketTransportAddress(InetAddress.getByName(host), port);
        } catch (UnknownHostException e) {
            throw new IllegalArgumentException("Failed to setup host address", e);
        }
        Client client = new PreBuiltTransportClient(settings).addTransportAddress(address);
        return client;
    }

    @Override
    public Client get() {
        return createClient();
    }
}
