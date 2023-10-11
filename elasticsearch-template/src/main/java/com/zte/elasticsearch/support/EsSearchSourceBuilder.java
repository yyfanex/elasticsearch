package com.zte.elasticsearch.support;

import org.elasticsearch.search.builder.SearchSourceBuilder;

public interface EsSearchSourceBuilder<T> {
    SearchSourceBuilder build(T entity);
}
