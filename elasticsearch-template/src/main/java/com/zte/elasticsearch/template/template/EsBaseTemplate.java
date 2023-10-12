package com.zte.elasticsearch.template.template;

import com.zte.elasticsearch.template.entity.PagedResponse;
import com.zte.elasticsearch.template.metadata.EsKey;
import com.zte.elasticsearch.template.metadata.EsScroll;
import com.zte.elasticsearch.template.support.EsConverter;
import com.zte.elasticsearch.template.support.EsSearchSourceBuilder;
import com.zte.elasticsearch.template.mapper.BaseMapper;
import org.elasticsearch.index.query.BoolQueryBuilder;
import org.elasticsearch.search.builder.SearchSourceBuilder;

import java.util.*;
import java.util.function.Consumer;

public class EsBaseTemplate<T extends EsKey> extends AbstractEsTemplate<T> implements BaseMapper<T> {
    private EsGetTemplate<T> getTemplate;
    private EsIndexTemplate<T> indexTemplate;
    private EsDeleteTemplate<T> deleteTemplate;
    private EsUpdateTemplate<T> updateTemplate;
    private EsSearchTemplate<T> searchTemplate;

    public EsBaseTemplate(Configuration configuration) {
        super(configuration);
        getTemplate = new EsGetTemplate<>(persistentClass, configuration);
        indexTemplate = new EsIndexTemplate<>(persistentClass, configuration);
        deleteTemplate = new EsDeleteTemplate<>(persistentClass, configuration);
        updateTemplate = new EsUpdateTemplate<>(persistentClass, configuration);
        searchTemplate = new EsSearchTemplate<>(persistentClass, configuration);
    }

    @Override
    public Optional<T> get(String id) {
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
    public void deleteByQuery(BoolQueryBuilder query) {
        deleteTemplate.deleteByQuery(query);
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
    public <E extends EsScroll> PagedResponse<T> scrollSearch(E request, EsSearchSourceBuilder<E> builder) {
        return searchTemplate.scrollSearch(request, builder);
    }

    @Override
    public <T, E extends EsScroll> PagedResponse<T> scrollSearch(Class<T> clazz, E request, EsSearchSourceBuilder<E> builder) {
        return searchTemplate.scrollSearch(clazz, request, builder);
    }

    @Override
    public <T1, E extends EsScroll> PagedResponse<T1> scrollSearch(Class<T1> clazz, E request, EsSearchSourceBuilder<E> builder, EsConverter<T1> converter) {
        return searchTemplate.scrollSearch(clazz, request, builder, converter);
    }

    @Override
    public <E extends EsScroll> void scrollAll(E request, EsSearchSourceBuilder<E> builder, Consumer<PagedResponse<T>> consumer) {
        searchTemplate.scrollAll(request, builder, consumer);
    }

    @Override
    public <T1, E extends EsScroll> void scrollAll(Class<T1> clazz, E request, EsSearchSourceBuilder<E> builder, Consumer<PagedResponse<T1>> consumer) {
        searchTemplate.scrollAll(clazz, request, builder, consumer);
    }

}

