package com.zte.elasticsearch.metadata;

/**
 * elasticsearch 并发数据对象
 * 请参考：https://www.elastic.co/guide/en/elasticsearch/reference/7.17/optimistic-concurrency-control.html
 */
public interface EsConcurrent {
    Long getSeqNo();
    void setSeqNo(Long seqNo);

    Long getPrimaryTerm();
    void setPrimaryTerm(Long primaryTerm);
}

