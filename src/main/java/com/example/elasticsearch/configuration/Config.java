package com.example.elasticsearch.configuration;

import lombok.extern.slf4j.Slf4j;
import org.elasticsearch.client.Client;
import org.elasticsearch.client.transport.TransportClient;
import org.elasticsearch.common.settings.Settings;
import org.elasticsearch.common.transport.TransportAddress;
import org.elasticsearch.transport.client.PreBuiltTransportClient;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.elasticsearch.core.ElasticsearchOperations;
import org.springframework.data.elasticsearch.core.ElasticsearchTemplate;

import java.net.InetAddress;

@Configuration
@Slf4j
public class Config {

    @Value("${elasticsearch.host}")
    private String hostname;

    @Value("${elasticsearch.port}")
    private String port;

    @Value("${elasticsearch.clustername}")
    private String clusterName;

    @Value("${elasticsearch.pool}")
    private String poolSize;

    /**
     * ??TransportClient??
     */
    @Bean(name = "transportClient")
    public TransportClient transportClient() {
        log.info("Elastic Search Start");
        TransportClient transportClient = null;
        try {
            Settings esSetting = Settings.builder()
                    .put("cluster.name", clusterName)
                    .put("client.transport.sniff", true)
                    .put("thread_pool.search.size", Integer.parseInt(poolSize))
                    .build();
            transportClient = new PreBuiltTransportClient(esSetting);
            TransportAddress transportAddress = new TransportAddress(InetAddress.getByName(hostname), Integer.valueOf(port));
            transportClient.addTransportAddresses(transportAddress);
        } catch (Exception e) {
            log.error("elasticsearch TransportClient create error!!", e);
        }
        return transportClient;
    }

    @Bean
    public ElasticsearchOperations elasticsearchTemplate() {
        Client client = transportClient();
        if (client != null) {
            return new ElasticsearchTemplate(client);
        } else {
            throw new RuntimeException("???Elasticsearch??!");
        }
    }

}
