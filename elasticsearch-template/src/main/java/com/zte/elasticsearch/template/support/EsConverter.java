package com.zte.elasticsearch.template.support;

import org.elasticsearch.action.search.SearchResponse;

import java.util.List;

public interface EsConverter<T> {
    List<T> convert(JsonMapper jsonMapper, SearchResponse searchResponse, Class<T> clazz);
}
