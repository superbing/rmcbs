package com.bfd.utils;

import lombok.extern.slf4j.Slf4j;
import org.elasticsearch.action.bulk.BackoffPolicy;
import org.elasticsearch.action.bulk.BulkProcessor;
import org.elasticsearch.action.bulk.BulkRequest;
import org.elasticsearch.action.bulk.BulkResponse;
import org.elasticsearch.client.Client;
import org.elasticsearch.common.unit.ByteSizeUnit;
import org.elasticsearch.common.unit.ByteSizeValue;
import org.elasticsearch.common.unit.TimeValue;

@Slf4j
public class EsUtils {
    
    public static BulkProcessor bulkProcessor(Client client) {
        BulkProcessor bulkProcessor = BulkProcessor.builder(client, new BulkProcessor.Listener() {
            @Override
            public void beforeBulk(long executionId, BulkRequest request) {
            }

            @Override
            public void afterBulk(long executionId, BulkRequest request, Throwable failure) {
                failure.printStackTrace();
            }

            @Override
            public void afterBulk(long executionId, BulkRequest request, BulkResponse response) {
                if (response.hasFailures()) {
                    log.error("Failure Actions: " + request.numberOfActions());
                    System.exit(500);
                }
            }
        }).setBulkActions(10000)//
            .setBulkSize(new ByteSizeValue(100, ByteSizeUnit.MB))//
            .setFlushInterval(TimeValue.timeValueSeconds(10))//
            .setConcurrentRequests(1)//
            .setBackoffPolicy(BackoffPolicy.exponentialBackoff(TimeValue.timeValueMillis(100), 3))//
            .build();
        
        return bulkProcessor;
    }
}
