package com.zte.elasticsearch.metadata;

public interface EsKey {
    default String primaryKey() {
        return null;
    }
}

