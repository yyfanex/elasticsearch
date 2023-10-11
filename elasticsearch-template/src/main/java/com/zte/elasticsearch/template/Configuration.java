package com.zte.elasticsearch.template;

import com.zte.elasticsearch.support.EsClient;
import com.zte.elasticsearch.support.JacksonJsonMapper;
import com.zte.elasticsearch.support.JsonMapper;
import org.elasticsearch.action.support.WriteRequest;
import org.elasticsearch.client.RestHighLevelClient;

public class Configuration {
    protected EsClient esClient;

    private JsonMapper jsonMapper;
    private JsonMapper defaultJsonMapper = new JacksonJsonMapper();

    private WriteRequest.RefreshPolicy refreshPolicy = WriteRequest.RefreshPolicy.IMMEDIATE;

    private Configuration() {

    }

    public static Configuration build() {
        return new Configuration();
    }

    public EsClient getEsClient() {
        return esClient;
    }

    public WriteRequest.RefreshPolicy getRefreshPolicy() {
        return refreshPolicy;
    }




    public Configuration esClient(RestHighLevelClient restClient) {
        this.esClient = new EsClient(restClient);
        return this;
    }

    public JsonMapper getJsonMapper() {
        if (jsonMapper == null) {
            return defaultJsonMapper;
        }
        return jsonMapper;
    }

    public Configuration jsonMapper(JsonMapper jsonMapper) {
        this.jsonMapper = jsonMapper;
        return this;
    }

    public Configuration refreshPolicy(WriteRequest.RefreshPolicy refreshPolicy) {
        this.refreshPolicy = refreshPolicy;
        return this;
    }
}
