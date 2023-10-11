package com.zte.elasticsearch.mapper;

import com.zte.elasticsearch.metadata.EsKey;
import org.elasticsearch.index.query.BoolQueryBuilder;

import java.util.List;

public interface DeleteMapper<T extends EsKey> {
    void delete(String key);

    default void delete(T record) {
        delete(record.primaryKey());
    }

    void bulkDelete(List<String> records);

    void deleteByQuery(BoolQueryBuilder query);
}
