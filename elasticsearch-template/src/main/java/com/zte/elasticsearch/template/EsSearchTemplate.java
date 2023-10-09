package com.zte.elasticsearch.template;

import com.zte.elasticsearch.entity.PagedResponse;
import com.zte.elasticsearch.mapper.SearchMapper;
import com.zte.elasticsearch.metadata.EsKey;
import com.zte.elasticsearch.metadata.EsScroll;
import com.zte.elasticsearch.support.EsConverter;
import com.zte.elasticsearch.support.EsSearchSourceBuilder;
import com.zte.elasticsearch.support.JsonMapper;
import com.zte.elasticsearch.utils.ConvertUtils;
import org.apache.commons.lang3.StringUtils;
import org.elasticsearch.action.search.SearchRequest;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.action.search.SearchScrollRequest;
import org.elasticsearch.common.unit.TimeValue;
import org.elasticsearch.search.builder.SearchSourceBuilder;

import java.util.List;
import java.util.concurrent.TimeUnit;

public class EsSearchTemplate<T extends EsKey> extends AbstractTemplate<T> implements SearchMapper<T> {
    public EsSearchTemplate(Configuration configuration) {
        super(configuration);
    }

    @Override
    public PagedResponse<T> search(SearchSourceBuilder sourceBuilder) {
        return search(persistentClass, sourceBuilder);
    }

    @Override
    public <E> PagedResponse<T> search(E request, EsSearchSourceBuilder<E> builder) {
        return search(persistentClass, request, builder);
    }

    @Override
    public PagedResponse<T> search(SearchSourceBuilder sourceBuilder, EsConverter<T> converter) {
        return search(sourceBuilder, persistentClass, converter);
    }

    @Override
    public <T1> PagedResponse<T1> search(Class<T1> clazz, SearchSourceBuilder sourceBuilder) {
        return search(sourceBuilder, clazz, (JsonMapper jsonMapper, SearchResponse searchResponse, Class<T1> cls) -> ConvertUtils.convert(jsonMapper, searchResponse, cls));
    }

    @Override
    public <T1, E> PagedResponse<T1> search(Class<T1> clazz, E request, EsSearchSourceBuilder<E> builder) {
        return search(builder.build(request), clazz, (JsonMapper jsonMapper, SearchResponse searchResponse, Class<T1> cls) -> ConvertUtils.convert(jsonMapper, searchResponse, cls));
    }

    @Override
    public <T1> PagedResponse<T1> search(SearchSourceBuilder sourceBuilder, Class<T1> persistentClass, EsConverter<T1> converter) {
        sourceBuilder.seqNoAndPrimaryTerm(true);
        SearchRequest searchRequest = new SearchRequest()
                .indices(index)
                .source(sourceBuilder);
        SearchResponse searchResponse = configuration.getEsClient().search(searchRequest);
        List<T1> list = converter.convert(configuration.getJsonMapper(), searchResponse, persistentClass);
        return PagedResponse.success(list, (int)searchResponse.getHits().getTotalHits().value, searchResponse.getScrollId());
    }

    @Override
    public <E extends EsScroll> PagedResponse<List<T>> scrollSearch(E request, EsSearchSourceBuilder<E> builder) {
        return scrollSearch(persistentClass, request, builder, ConvertUtils::convert);
    }

    @Override
    public <T1, E extends EsScroll> PagedResponse<List<T1>> scrollSearch(Class<T1> clazz, E request, EsSearchSourceBuilder<E> builder) {
        return scrollSearch(clazz, request, builder, ConvertUtils::convert);
    }

    @Override
    public <T1, E extends EsScroll> PagedResponse<List<T1>> scrollSearch(Class<T1> clazz, E request, EsSearchSourceBuilder<E> builder, EsConverter<T1> converter) {
        SearchResponse searchResponse;
        if (request.getScroll() != null && request.getScroll() && StringUtils.isNotBlank(request.getScrollId())) {
            SearchScrollRequest scrollRequest = new SearchScrollRequest(request.getScrollId())
                    .scroll(new TimeValue(request.getDurationSeconds(), TimeUnit.SECONDS));
            searchResponse = configuration.getEsClient().scroll(scrollRequest);
        }
        else {
            SearchSourceBuilder sourceBuilder = builder.build(request);
            SearchRequest searchRequest = new SearchRequest()
                    .indices(index)
                    .source(sourceBuilder);
            if (request.getScroll() != null && request.getScroll()) {
                searchRequest.scroll(new TimeValue(request.getDurationSeconds(), TimeUnit.SECONDS));
            }
            searchResponse = configuration.getEsClient().search(searchRequest);
            request.setScrollId(searchResponse.getScrollId());
        }
        List<T1> list = converter.convert(configuration.getJsonMapper(), searchResponse, clazz);
        return PagedResponse.success(list, (int)searchResponse.getHits().getTotalHits().value, searchResponse.getScrollId());
    }
}
