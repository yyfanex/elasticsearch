package com.zte.elasticsearch.template;

import com.zte.elasticsearch.mapper.GetMapper;
import com.zte.elasticsearch.metadata.EsKey;
import com.zte.elasticsearch.utils.ConvertUtils;
import org.elasticsearch.action.get.GetRequest;
import org.elasticsearch.action.get.GetResponse;
import org.elasticsearch.action.get.MultiGetRequest;
import org.elasticsearch.action.get.MultiGetResponse;

import java.util.List;

public class EsGetTemplate<T extends EsKey> extends AbstractTemplate<T> implements GetMapper<T> {

    public EsGetTemplate(Configuration configuration) {
        super(configuration);
    }

    @Override
    public T get(String id) {
        GetRequest request = new GetRequest()
                .index(index)
                .id(id);
        GetResponse getResponse = configuration.getEsClient().get(request);
        return ConvertUtils.convert(configuration.getJsonMapper(), getResponse, persistentClass);
    }

    @Override
    public List<T> multipleGet(List<String> records) {
        MultiGetRequest request = new MultiGetRequest();
        for (String id : records) {
            request.add(new MultiGetRequest.Item(index, id));
        }
        MultiGetResponse response = configuration.getEsClient().multiGet(request);
        return ConvertUtils.convert(configuration.getJsonMapper(), response, persistentClass);
    }
}
