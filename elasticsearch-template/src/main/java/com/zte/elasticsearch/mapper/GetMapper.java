package com.zte.elasticsearch.mapper;

import com.zte.elasticsearch.metadata.EsKey;

import java.util.List;

public interface GetMapper<T extends EsKey> {
    T get(String key);

    default T get(T record) {
        return get(record.primaryKey());
    }

    List<T> multipleGet(List<String> records);
}
