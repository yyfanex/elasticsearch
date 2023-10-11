package io.github.yyfanex;

import org.apache.http.HttpHost;
import org.elasticsearch.client.RestClient;
import org.elasticsearch.client.RestClientBuilder;
import org.elasticsearch.client.RestHighLevelClient;

public class HighLevelClientFactory {
    public static HighLevelClientFactory build() {
        return new HighLevelClientFactory();
    }

    public RestHighLevelClient newClient() {
        RestClientBuilder clientBuilder = RestClient.builder(HttpHost.create("10.54.147.135:9204"));
        RestHighLevelClient restHighLevelClient = new RestHighLevelClient(clientBuilder);
        return restHighLevelClient;
    }
}
