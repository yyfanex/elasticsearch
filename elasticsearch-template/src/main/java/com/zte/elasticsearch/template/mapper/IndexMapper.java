package com.zte.elasticsearch.template.mapper;

import com.zte.elasticsearch.template.metadata.EsKey;

import java.util.List;

public interface IndexMapper<T extends EsKey> {
    void index(T entity);

    void bulkIndex(List<T> records);
}
