package com.zte.elasticsearch.support;

import org.elasticsearch.action.admin.cluster.snapshots.create.CreateSnapshotRequest;
import org.elasticsearch.action.admin.cluster.snapshots.create.CreateSnapshotResponse;
import org.elasticsearch.action.admin.cluster.snapshots.delete.DeleteSnapshotRequest;
import org.elasticsearch.action.bulk.BulkRequest;
import org.elasticsearch.action.bulk.BulkResponse;
import org.elasticsearch.action.delete.DeleteRequest;
import org.elasticsearch.action.delete.DeleteResponse;
import org.elasticsearch.action.explain.ExplainRequest;
import org.elasticsearch.action.explain.ExplainResponse;
import org.elasticsearch.action.get.GetRequest;
import org.elasticsearch.action.get.GetResponse;
import org.elasticsearch.action.get.MultiGetRequest;
import org.elasticsearch.action.get.MultiGetResponse;
import org.elasticsearch.action.index.IndexRequest;
import org.elasticsearch.action.index.IndexResponse;
import org.elasticsearch.action.search.SearchRequest;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.action.search.SearchScrollRequest;
import org.elasticsearch.action.support.master.AcknowledgedResponse;
import org.elasticsearch.action.update.UpdateRequest;
import org.elasticsearch.action.update.UpdateResponse;
import org.elasticsearch.client.RequestOptions;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.client.core.TermVectorsRequest;
import org.elasticsearch.client.core.TermVectorsResponse;
import org.elasticsearch.index.reindex.BulkByScrollResponse;
import org.elasticsearch.index.reindex.DeleteByQueryRequest;
import org.elasticsearch.script.mustache.SearchTemplateRequest;
import org.elasticsearch.script.mustache.SearchTemplateResponse;

public class EsClient {
    private RestHighLevelClient restHighLevelClient;

    public EsClient(RestHighLevelClient restHighLevelClient) {
        this.restHighLevelClient = restHighLevelClient;
    }

    public RestHighLevelClient highLevelClient() {
        return restHighLevelClient;
    }

    public BulkResponse bulk(BulkRequest bulkRequest, RequestOptions options) {
        try {
            return restHighLevelClient.bulk(bulkRequest, options);
        }
        catch (Exception e) {
            throw new EsRequestException("Error bulk request. Cause: " + e, e);
        }
    }

    public SearchResponse search(SearchRequest searchRequest) {
        try {
            SearchResponse searchResponse = restHighLevelClient.search(searchRequest, RequestOptions.DEFAULT);
            return searchResponse;
        }
        catch (Exception e) {
            throw new EsRequestException("Error search request. Cause: " + e, e);
        }
    }

    public SearchResponse scroll(SearchScrollRequest searchRequest) {
        try {
            SearchResponse searchResponse = restHighLevelClient.scroll(searchRequest, RequestOptions.DEFAULT);
            return searchResponse;
        }
        catch (Exception e) {
            throw new EsRequestException("Error scroll request. Cause: " + e, e);
        }
    }

    public IndexResponse index(IndexRequest indexRequest) {
        try {
            IndexResponse indexResponse = restHighLevelClient.index(indexRequest, RequestOptions.DEFAULT);
            return indexResponse;
        }
        catch (Exception e) {
            throw new EsRequestException("Error index request " + indexRequest.index() + "." + indexRequest.id() + ". Cause: " + e, e);
        }
    }

    public DeleteResponse delete(DeleteRequest deleteRequest) {
        try {
            DeleteResponse deleteResponse = restHighLevelClient.delete(deleteRequest, RequestOptions.DEFAULT);
            return deleteResponse;
        }
        catch (Exception e) {
            throw new EsRequestException("Error delete request " + deleteRequest.index() + "." + deleteRequest.id() + ". Cause: " + e, e);
        }
    }

    public UpdateResponse update(UpdateRequest updateRequest) {
        try {
            return restHighLevelClient.update(updateRequest, RequestOptions.DEFAULT);
        }
        catch (Exception e) {
            throw new EsRequestException("Error update request " + updateRequest.index() + "." + updateRequest.id() + ". Cause: " + e, e);
        }

    }

    public GetResponse get(GetRequest getRequest) {
        try {
            return restHighLevelClient.get(getRequest, RequestOptions.DEFAULT);
        }
        catch (Exception e) {
            throw new EsRequestException("Error get request " + getRequest.index() + "." + getRequest.id() + ". Cause: " + e, e);
        }
    }

    public ExplainResponse explain(ExplainRequest explainRequest) {
        try {
            return restHighLevelClient.explain(explainRequest, RequestOptions.DEFAULT);
        }
        catch (Exception e) {
            throw new EsRequestException("Error explain request " + explainRequest.index() + "." + explainRequest.id() + ". Cause: " + e, e);
        }
    }

    public TermVectorsResponse termVectors(TermVectorsRequest request) {
        try {
            return restHighLevelClient.termvectors(request, RequestOptions.DEFAULT);
        }
        catch (Exception e) {
            throw new EsRequestException("Error termvectors request. Cause: " + e, e);
        }
    }

    public BulkByScrollResponse deleteByQuery(DeleteByQueryRequest request) {
        try {
            return restHighLevelClient.deleteByQuery(request, RequestOptions.DEFAULT);
        }
        catch (Exception e) {
            throw new EsRequestException("Error deleteByQuery request. Cause: " + e, e);
        }
    }

    public MultiGetResponse multiGet(MultiGetRequest request) {
        try {
            return restHighLevelClient.mget(request, RequestOptions.DEFAULT);
        }
        catch (Exception e) {
            throw new EsRequestException("Error multiGet request. Cause: " + e, e);
        }
    }

    /**
     * 模板查询
     * 参考：https://www.elastic.co/guide/en/elasticsearch/client/java-rest/current/java-rest-high-search-template.html
     * @param request
     * @return
     */
    public SearchTemplateResponse searchTemplate(SearchTemplateRequest request) {
        try {
            return restHighLevelClient.searchTemplate(request, RequestOptions.DEFAULT);
        }
        catch (Exception e) {
            throw new EsRequestException("Error searchTemplate request. Cause: " + e, e);
        }
    }
}
