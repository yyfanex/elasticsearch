package com.zte.elasticsearch.template;

import com.zte.elasticsearch.mapper.DeleteMapper;
import com.zte.elasticsearch.mapper.GetMapper;
import com.zte.elasticsearch.metadata.EsKey;
import com.zte.elasticsearch.utils.ResponseUtils;
import org.elasticsearch.action.DocWriteRequest;
import org.elasticsearch.action.delete.DeleteRequest;
import org.elasticsearch.action.delete.DeleteResponse;
import org.elasticsearch.action.support.WriteRequest;
import org.elasticsearch.index.query.BoolQueryBuilder;
import org.elasticsearch.index.reindex.BulkByScrollResponse;
import org.elasticsearch.index.reindex.DeleteByQueryRequest;

import java.util.ArrayList;
import java.util.List;

public class EsDeleteTemplate<T extends EsKey> extends AbstractTemplate<T> implements DeleteMapper<T> {
    public EsDeleteTemplate(Configuration configuration) {
        super(configuration);
    }

    @Override
    public void delete(String key) {
        DeleteRequest request = new DeleteRequest(index)
                .id(key);
        request.setRefreshPolicy(WriteRequest.RefreshPolicy.IMMEDIATE);
        DeleteResponse deleteResponse = configuration.getEsClient().delete(request);
        ResponseUtils.handleResponse(deleteResponse);
    }

    @Override
    public void bulkDelete(List<String> records) {
        List<DocWriteRequest<?>> requests = new ArrayList<>();
        for (String id : records) {
            requests.add(new DeleteRequest(index).id(id));
        }
        bulkRequest(requests);
    }

    @Override
    public void deleteByQuery(BoolQueryBuilder query) {
        DeleteByQueryRequest request = new DeleteByQueryRequest(index);
        request.setQuery(query);
        request.setRefresh(true);
        BulkByScrollResponse bulkResponse = configuration.getEsClient().deleteByQuery(request);
        ResponseUtils.handleResponse(bulkResponse);
    }
}
