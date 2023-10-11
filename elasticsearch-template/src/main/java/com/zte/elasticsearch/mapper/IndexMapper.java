package com.zte.elasticsearch.mapper;

import com.zte.elasticsearch.metadata.EsKey;

import java.util.List;

public interface IndexMapper<T extends EsKey> {
    void index(T entity);

    void bulkIndex(List<T> records);
}
