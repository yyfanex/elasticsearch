package com.zte.elasticsearch.template;

import com.sun.istack.internal.NotNull;
import com.zte.elasticsearch.support.EsClient;
import com.zte.elasticsearch.support.JacksonJsonMapper;
import com.zte.elasticsearch.support.JsonMapper;
import org.elasticsearch.client.RestHighLevelClient;

public class Configuration {
    @NotNull
    protected EsClient esClient;

    @NotNull
    private JsonMapper jsonMapper;
    private JsonMapper defaultJsonMapper;

    private Configuration() {
        defaultJsonMapper = new JacksonJsonMapper();
    }

    public static Configuration build() {
        return new Configuration();
    }

    public EsClient getEsClient() {
        return esClient;
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
}
