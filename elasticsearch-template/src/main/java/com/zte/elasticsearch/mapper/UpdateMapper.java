package com.zte.elasticsearch.mapper;

import com.zte.elasticsearch.metadata.EsKey;

import java.util.List;
import java.util.Map;

public interface UpdateMapper<T extends EsKey> {
    void update(String primaryKey, Map<String, Object> params, String script);

    void update(T entity, String script);

    void bulkUpdate(List<T> records, String script);

}
