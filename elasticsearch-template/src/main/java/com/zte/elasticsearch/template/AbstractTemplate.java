package com.zte.elasticsearch.template;

import com.zte.elasticsearch.metadata.EsKey;
import com.zte.elasticsearch.utils.ResponseUtils;
import org.elasticsearch.action.DocWriteRequest;
import org.elasticsearch.action.bulk.BulkRequest;
import org.elasticsearch.action.bulk.BulkResponse;
import org.elasticsearch.action.support.WriteRequest;
import org.elasticsearch.client.RequestOptions;

import javax.persistence.Table;
import java.lang.reflect.ParameterizedType;
import java.util.Arrays;
import java.util.List;

class AbstractTemplate<T extends EsKey> {
    protected String index;

    protected Class<T> persistentClass;

    protected Configuration configuration;

    public AbstractTemplate(Configuration configuration) {
        this.configuration = configuration;
        this.persistentClass = (Class<T>) ((ParameterizedType) getClass().getGenericSuperclass()).getActualTypeArguments()[0];
        Table tableAnnotation = persistentClass.getAnnotation(Table.class);
        index = tableAnnotation.name();
    }

    protected void bulkRequest(DocWriteRequest<?>... requests) {
        BulkRequest bulkRequest = new BulkRequest();
        Arrays.stream(requests).forEach(request -> bulkRequest.add(request));
        bulkRequest.setRefreshPolicy(WriteRequest.RefreshPolicy.IMMEDIATE);
        BulkResponse bulkResponse = configuration.getEsClient().bulk(bulkRequest, RequestOptions.DEFAULT);
        ResponseUtils.handleResponse(bulkResponse);
    }

    protected void bulkRequest(List<DocWriteRequest<?>> requests) {
        BulkRequest bulkRequest = new BulkRequest();
        requests.stream().forEach(request -> bulkRequest.add(request));
        bulkRequest.setRefreshPolicy(WriteRequest.RefreshPolicy.IMMEDIATE);
        BulkResponse bulkResponse = configuration.getEsClient().bulk(bulkRequest, RequestOptions.DEFAULT);
        ResponseUtils.handleResponse(bulkResponse);
    }
}
