package com.bfd.config;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.elasticsearch.action.bulk.BackoffPolicy;
import org.elasticsearch.action.bulk.BulkProcessor;
import org.elasticsearch.action.bulk.BulkRequest;
import org.elasticsearch.action.bulk.BulkResponse;
import org.elasticsearch.client.transport.TransportClient;
import org.elasticsearch.common.settings.Settings;
import org.elasticsearch.common.transport.InetSocketTransportAddress;
import org.elasticsearch.common.unit.ByteSizeUnit;
import org.elasticsearch.common.unit.ByteSizeValue;
import org.elasticsearch.common.unit.TimeValue;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.data.elasticsearch.core.ElasticsearchTemplate;

import java.net.InetAddress;
import java.net.UnknownHostException;

/**
 * ES配置
 * 
 * @author 姓名 工号
 * @version [版本号, 2018年8月9日]
 * @see [相关类/方法]
 * @since [产品/模块版本]
 */
@Configuration
@Slf4j
public class EsConfig {
    
    @Value(value = "${es.cluster.name}")
    private String clusterName;
    
    @Value(value = "${es.cluster.ips}")
    private String ips;
    
    @Value(value = "${es.cluster.port}")
    private String port;
    
    public static final String CLUSTER_NAME = "cluster.name";
    
    public static final String CLIENT_TRANSPORT_SNIFF = "client.transport.sniff";
    
    public static final String CLIENT_TRANSPORT_PING_TIMEOUT = "client.transport.ping_timeout";
    
    public static final String CLIENT_TRANSPORT_NODES_SAMPLER_INTERVAL = "client.transport.nodes_sampler_interval";
    
    @Bean
    ElasticsearchTemplate elasticsearchTemplate(@Qualifier("transportClient") TransportClient transportClient) {
        return new ElasticsearchTemplate(transportClient);
    }
    
    @Bean(name = "transportClient")
    @Primary
    public TransportClient transportClient() throws UnknownHostException {
        log.info("clusterName---->" + clusterName);
        return this.getClient(clusterName, ips, port);
    }
    
    private TransportClient getClient(String clusterName, String ips, String port) throws UnknownHostException {
        Settings settings = Settings.settingsBuilder()//
            .put(CLUSTER_NAME, clusterName)//
            .put(CLIENT_TRANSPORT_SNIFF, true)//
            .put(CLIENT_TRANSPORT_PING_TIMEOUT, "1800s")//
            .put(CLIENT_TRANSPORT_NODES_SAMPLER_INTERVAL, "1800s")//
            .build();
        
        TransportClient client = TransportClient.builder().settings(settings).build();
        if (StringUtils.isNotEmpty(ips)) {
            String[] ipArray = ips.split(",");
            for (String ip : ipArray) {
                InetAddress inetAddress = InetAddress.getByName(ip);
                int inetPort = Integer.parseInt(port);
                InetSocketTransportAddress address = new InetSocketTransportAddress(inetAddress, inetPort);
                client.addTransportAddress(address);
            }
        }
        return client;
    }

    @Bean
    public BulkProcessor bulkProcessor(@Qualifier("transportClient") TransportClient client) {
        return BulkProcessor.builder(client, new BulkProcessor.Listener() {
            @Override
            public void beforeBulk(long l, BulkRequest bulkRequest) {

            }
            @Override
            public void afterBulk(long l, BulkRequest bulkRequest, BulkResponse bulkResponse) {

            }
            @Override
            public void afterBulk(long l, BulkRequest bulkRequest, Throwable throwable) {
                log.error("{} data bulk failed,reason :{}", bulkRequest.numberOfActions(), throwable);
            }
        }).setBulkActions(1000)
            .setBulkSize(new ByteSizeValue(5, ByteSizeUnit.MB))
            .setFlushInterval(TimeValue.timeValueSeconds(5))
            .setConcurrentRequests(1)
            .setBackoffPolicy(BackoffPolicy.exponentialBackoff(TimeValue.timeValueMillis(100), 3))
            .build();
    }
}
