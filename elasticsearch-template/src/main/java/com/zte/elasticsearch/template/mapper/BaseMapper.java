package com.zte.elasticsearch.template.mapper;

import com.zte.elasticsearch.template.metadata.EsKey;

public interface BaseMapper<T extends EsKey> extends
        GetMapper<T>,
        IndexMapper<T>,
        DeleteMapper<T>,
        UpdateMapper<T>,
        SearchMapper<T> {
}
