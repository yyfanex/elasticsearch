package com.zte.elasticsearch.template.metadata;

public interface EsKey {
    default String primaryKey() {
        return null;
    }
}

