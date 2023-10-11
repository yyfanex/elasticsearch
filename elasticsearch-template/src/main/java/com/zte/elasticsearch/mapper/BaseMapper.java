package com.zte.elasticsearch.mapper;

import com.zte.elasticsearch.metadata.EsKey;

public interface BaseMapper<T extends EsKey> extends
        GetMapper<T>,
        IndexMapper<T>,
        DeleteMapper<T>,
        UpdateMapper<T>,
        SearchMapper<T> {
}
