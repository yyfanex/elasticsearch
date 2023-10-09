package com.zte.elasticsearch.template;

import com.zte.elasticsearch.mapper.GetMapper;
import com.zte.elasticsearch.mapper.IndexMapper;
import com.zte.elasticsearch.metadata.EsConcurrent;
import com.zte.elasticsearch.metadata.EsKey;
import com.zte.elasticsearch.utils.ResponseUtils;
import org.elasticsearch.action.DocWriteRequest;
import org.elasticsearch.action.index.IndexRequest;
import org.elasticsearch.action.index.IndexResponse;
import org.elasticsearch.action.support.WriteRequest;
import org.elasticsearch.common.xcontent.XContentType;

import java.util.ArrayList;
import java.util.List;

public class EsIndexTemplate<T extends EsKey> extends AbstractTemplate<T> implements IndexMapper<T> {

    public EsIndexTemplate(Configuration configuration) {
        super(configuration);
    }

    @Override
    public void index(T entity) {
        IndexRequest indexRequest = buildIndexRequest(entity);
        indexRequest.setRefreshPolicy(WriteRequest.RefreshPolicy.IMMEDIATE);
        IndexResponse indexResponse = configuration.getEsClient().index(indexRequest);
        ResponseUtils.handleResponse(indexResponse);
    }

    @Override
    public void bulkIndex(List<T> records) {
        List<DocWriteRequest<?>> requests = new ArrayList<>();
        for (T entity : records) {
            requests.add(buildIndexRequest(entity));
        }
        bulkRequest(requests);
    }

    protected IndexRequest buildIndexRequest(T entity) {
        IndexRequest indexRequest = new IndexRequest(index)
                .id(entity.primaryKey())
                .source(configuration.getJsonMapper().map(entity), XContentType.JSON);
        //并发控制
        if (EsConcurrent.class.isAssignableFrom(persistentClass)) {
            EsConcurrent concurrent = (EsConcurrent) entity;
            if (concurrent.getSeqNo() != null && concurrent.getSeqNo() > 0 && concurrent.getPrimaryTerm() != null && concurrent.getPrimaryTerm() >= 0) {
                indexRequest.setIfSeqNo(concurrent.getSeqNo());
                indexRequest.setIfPrimaryTerm(concurrent.getPrimaryTerm());
            }
        }
        return indexRequest;
    }
}
