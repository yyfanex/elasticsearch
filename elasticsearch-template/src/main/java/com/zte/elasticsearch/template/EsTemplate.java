package com.zte.elasticsearch.template;

import com.zte.elasticsearch.entity.PagedResponse;
import com.zte.elasticsearch.mapper.*;
import com.zte.elasticsearch.metadata.*;
import com.zte.elasticsearch.support.EsConverter;
import com.zte.elasticsearch.support.EsSearchSourceBuilder;
import com.zte.elasticsearch.support.JsonMapper;
import com.zte.elasticsearch.utils.CollectionsUtil;
import com.zte.elasticsearch.utils.ConvertUtils;
import com.zte.elasticsearch.utils.ResponseUtils;
import org.apache.commons.lang3.StringUtils;
import org.elasticsearch.action.DocWriteRequest;
import org.elasticsearch.action.bulk.BulkRequest;
import org.elasticsearch.action.bulk.BulkResponse;
import org.elasticsearch.action.delete.DeleteRequest;
import org.elasticsearch.action.delete.DeleteResponse;
import org.elasticsearch.action.get.GetRequest;
import org.elasticsearch.action.get.GetResponse;
import org.elasticsearch.action.get.MultiGetRequest;
import org.elasticsearch.action.get.MultiGetResponse;
import org.elasticsearch.action.index.IndexRequest;
import org.elasticsearch.action.index.IndexResponse;
import org.elasticsearch.action.search.SearchRequest;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.action.search.SearchScrollRequest;
import org.elasticsearch.action.support.WriteRequest;
import org.elasticsearch.action.update.UpdateRequest;
import org.elasticsearch.action.update.UpdateResponse;
import org.elasticsearch.client.RequestOptions;
import org.elasticsearch.common.unit.TimeValue;
import org.elasticsearch.common.xcontent.XContentType;
import org.elasticsearch.index.query.BoolQueryBuilder;
import org.elasticsearch.index.reindex.BulkByScrollResponse;
import org.elasticsearch.index.reindex.DeleteByQueryRequest;
import org.elasticsearch.script.Script;
import org.elasticsearch.search.builder.SearchSourceBuilder;

import javax.persistence.Table;
import java.lang.reflect.ParameterizedType;
import java.util.*;
import java.util.concurrent.TimeUnit;
import java.util.function.Consumer;

public final class EsTemplate<T extends EsKey> extends AbstractTemplate<T> implements BaseMapper<T>{
    private EsGetTemplate<T> getTemplate;
    private EsIndexTemplate<T> indexTemplate;
    private EsDeleteTemplate<T> deleteTemplate;
    private EsUpdateTemplate<T> updateTemplate;
    private EsSearchTemplate<T> searchTemplate;

    public EsTemplate(Configuration configuration) {
        super(configuration);
        getTemplate = new EsGetTemplate<>(configuration);
        indexTemplate = new EsIndexTemplate<>(configuration);
        deleteTemplate = new EsDeleteTemplate<>(configuration);
        updateTemplate = new EsUpdateTemplate<>(configuration);
        searchTemplate = new EsSearchTemplate<>(configuration);
    }

    @Override
    public T get(String id) {
        return getTemplate.get(id);
    }

    @Override
    public void index(T entity) {
        indexTemplate.index(entity);
    }

    @Override
    public void delete(String key) {
        deleteTemplate.delete(key);
    }

    @Override
    public void update(T entity, String script) {
        updateTemplate.update(entity, script);
    }

    @Override
    public void update(String primaryKey, Map<String, Object> params, String script) {
        updateTemplate.update(primaryKey, params, script);
    }

    @Override
    public void bulkIndex(List<T> records) {
        indexTemplate.bulkIndex(records);
    }

    @Override
    public void bulkUpdate(List<T> records, String script) {
        updateTemplate.bulkUpdate(records, script);
    }

    @Override
    public void bulkDelete(List<String> records) {
        deleteTemplate.bulkDelete(records);
    }

    @Override
    public List<T> multipleGet(List<String> records) {
        return getTemplate.multipleGet(records);
    }

    @Override
    public PagedResponse<T> search(SearchSourceBuilder sourceBuilder) {
        return searchTemplate.search(sourceBuilder);
    }

    @Override
    public PagedResponse<T> search(SearchSourceBuilder sourceBuilder, EsConverter<T> converter) {
        return searchTemplate.search(sourceBuilder, converter);
    }

    @Override
    public <E> PagedResponse<T> search(E request, EsSearchSourceBuilder<E> builder) {
        return searchTemplate.search(request, builder);
    }

    @Override
    public <T, E> PagedResponse<T> search(Class<T> clazz, E request, EsSearchSourceBuilder<E> builder) {
        return searchTemplate.search(clazz, request, builder);
    }

    @Override
    public <T> PagedResponse<T> search(Class<T> clazz, SearchSourceBuilder sourceBuilder) {
        return searchTemplate.search(clazz, sourceBuilder);
    }

    @Override
    public <T1> PagedResponse<T1> search(SearchSourceBuilder sourceBuilder, Class<T1> persistentClass, EsConverter<T1> converter) {
        return searchTemplate.search(sourceBuilder, persistentClass, converter);
    }

    @Override
    public <E extends EsScroll> PagedResponse<List<T>> scrollSearch(E request, EsSearchSourceBuilder<E> builder) {
        return searchTemplate.scrollSearch(request, builder);
    }

    @Override
    public <T, E extends EsScroll> PagedResponse<List<T>> scrollSearch(Class<T> clazz, E request, EsSearchSourceBuilder<E> builder) {
        return searchTemplate.scrollSearch(clazz, request, builder);
    }

    @Override
    public <T1, E extends EsScroll> PagedResponse<List<T1>> scrollSearch(Class<T1> clazz, E request, EsSearchSourceBuilder<E> builder, EsConverter<T1> converter) {
        return searchTemplate.scrollSearch(clazz, request, builder, converter);
    }

    @Override
    public void deleteByQuery(BoolQueryBuilder query) {
        deleteTemplate.deleteByQuery(query);
    }

}

