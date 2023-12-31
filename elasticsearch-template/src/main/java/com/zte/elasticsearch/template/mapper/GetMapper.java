package com.zte.elasticsearch.template.mapper;

import com.zte.elasticsearch.template.metadata.EsKey;

import java.util.List;
import java.util.Optional;

public interface GetMapper<T extends EsKey> {
    Optional<T> get(String key);

    default Optional<T> get(T record) {
        return get(record.primaryKey());
    }

    List<T> multipleGet(List<String> records);
}
