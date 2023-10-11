package com.zte.elasticsearch.utils;

import org.apache.commons.lang3.StringUtils;
import org.elasticsearch.action.DocWriteResponse;
import org.elasticsearch.action.bulk.BulkItemResponse;
import org.elasticsearch.action.bulk.BulkResponse;
import org.elasticsearch.action.support.replication.ReplicationResponse;
import org.elasticsearch.index.reindex.BulkByScrollResponse;

public class ResponseUtils {
    public static void handleResponse(DocWriteResponse response) {
        if (response == null) {
            return;
        }
        ReplicationResponse.ShardInfo shardInfo = response.getShardInfo();
        StringBuilder str = new StringBuilder();
        if (shardInfo.getFailed() > 0) {
            for (ReplicationResponse.ShardInfo.Failure failure : shardInfo.getFailures()) {
                str.append(failure.reason()).append(System.getProperty("line.separator"));
            }
        }
        String error = str.toString();
        if (StringUtils.isBlank(error)) {
            return;
        }
        throw new RuntimeException(error);
    }

    public static void handleResponse(BulkResponse bulkResponse) {
        if (bulkResponse == null) {
            return;
        }
        StringBuilder str = new StringBuilder();
        for (BulkItemResponse bulkItemResponse : bulkResponse) {
            if (bulkItemResponse.getFailure() != null) {
                BulkItemResponse.Failure failure = bulkItemResponse.getFailure();
                str.append(bulkItemResponse.getFailureMessage()).append(System.getProperty("line.separator"));
            }
        }
        if (StringUtils.isNotBlank(str.toString().trim())) {
            throw new RuntimeException(str.toString().trim());
        }
    }

    public static void handleResponse(BulkByScrollResponse response) {
        if (response == null) {
            return;
        }
        boolean timedOut = response.isTimedOut();
        if (timedOut) {
            throw new RuntimeException("elasticsearch timeout");
        }
        StringBuilder str = new StringBuilder();
        for (BulkItemResponse.Failure bulkFailure : response.getBulkFailures()) {
            str.append(bulkFailure.getMessage()).append(System.getProperty("line.separator"));
        }
        if (StringUtils.isNotBlank(str.toString().trim())) {
            throw new RuntimeException(str.toString().trim());
        }
    }
}
