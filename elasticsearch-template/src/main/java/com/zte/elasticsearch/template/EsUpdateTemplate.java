package com.zte.elasticsearch.template;

import com.zte.elasticsearch.mapper.GetMapper;
import com.zte.elasticsearch.mapper.UpdateMapper;
import com.zte.elasticsearch.metadata.EsConcurrent;
import com.zte.elasticsearch.metadata.EsKey;
import com.zte.elasticsearch.utils.ResponseUtils;
import org.elasticsearch.action.DocWriteRequest;
import org.elasticsearch.action.support.WriteRequest;
import org.elasticsearch.action.update.UpdateRequest;
import org.elasticsearch.action.update.UpdateResponse;
import org.elasticsearch.script.Script;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class EsUpdateTemplate<T extends EsKey> extends AbstractTemplate<T> implements UpdateMapper<T> {
    public EsUpdateTemplate(Configuration configuration) {
        super(configuration);
    }

    public EsUpdateTemplate(Class<T> clazz, Configuration configuration) {
        super(clazz, configuration);
    }

    @Override
    public void update(String primaryKey, Map<String, Object> params, String script) {
        UpdateRequest updateRequest = new UpdateRequest()
                .index(index)
                .id(primaryKey)
                .script(new Script(Script.DEFAULT_SCRIPT_TYPE, Script.DEFAULT_SCRIPT_LANG, script, params));
        updateRequest.setRefreshPolicy(configuration.getRefreshPolicy());
        UpdateResponse updateResponse = configuration.getEsClient().update(updateRequest);
        ResponseUtils.handleResponse(updateResponse);
    }

    @Override
    public void update(T entity, String script) {
        UpdateRequest updateRequest = buildUpdateRequest(entity, script);
        updateRequest.setRefreshPolicy(configuration.getRefreshPolicy());
        UpdateResponse updateResponse = configuration.getEsClient().update(updateRequest);
        ResponseUtils.handleResponse(updateResponse);
    }

    @Override
    public void bulkUpdate(List<T> records, String script) {
        List<DocWriteRequest<?>> requests = new ArrayList<>();
        for (T entity : records) {
            requests.add(buildUpdateRequest(entity, script));
        }
        bulkRequest(requests);
    }

    protected UpdateRequest buildUpdateRequest(T entity, String script) {
        UpdateRequest updateRequest = new UpdateRequest()
                .index(index)
                .id(entity.primaryKey())
                .script(new Script(Script.DEFAULT_SCRIPT_TYPE, Script.DEFAULT_SCRIPT_LANG, script, configuration.getJsonMapper().map(entity)));
        //并发控制
        if (EsConcurrent.class.isAssignableFrom(persistentClass)) {
            EsConcurrent concurrent = (EsConcurrent) entity;
            if (concurrent.getSeqNo() != null && concurrent.getSeqNo() > 0 && concurrent.getPrimaryTerm() != null && concurrent.getPrimaryTerm() >= 0) {
                updateRequest.setIfSeqNo(concurrent.getSeqNo());
                updateRequest.setIfPrimaryTerm(concurrent.getPrimaryTerm());
            }
        }
        return updateRequest;
    }
}
