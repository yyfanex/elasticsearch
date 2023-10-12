package com.zte.elasticsearch.template.mapper;

import com.zte.elasticsearch.template.entity.PagedResponse;
import com.zte.elasticsearch.template.metadata.EsScroll;
import com.zte.elasticsearch.template.support.EsConverter;
import com.zte.elasticsearch.template.support.EsSearchSourceBuilder;
import org.elasticsearch.search.builder.SearchSourceBuilder;

import java.util.function.Consumer;

public interface SearchMapper<T> {
    PagedResponse<T> search(SearchSourceBuilder sourceBuilder);

    PagedResponse<T> search(SearchSourceBuilder sourceBuilder, EsConverter<T> converter);

    <E> PagedResponse<T> search(E request, EsSearchSourceBuilder<E> builder);

    <T1, E> PagedResponse<T1> search(Class<T1> clazz, E request, EsSearchSourceBuilder<E> builder);

    <T1> PagedResponse<T1> search(Class<T1> clazz, SearchSourceBuilder sourceBuilder);

    <T1> PagedResponse<T1> search(SearchSourceBuilder sourceBuilder, Class<T1> persistentClass, EsConverter<T1> converter);

    <E extends EsScroll> PagedResponse<T> scrollSearch(E request, EsSearchSourceBuilder<E> builder);

    <T1, E extends EsScroll> PagedResponse<T1> scrollSearch(Class<T1> clazz, E request, EsSearchSourceBuilder<E> builder);

    <T1, E extends EsScroll> PagedResponse<T1> scrollSearch(Class<T1> clazz, E request, EsSearchSourceBuilder<E> builder, EsConverter<T1> converter);

    <E extends EsScroll> void scrollAll(E request, EsSearchSourceBuilder<E> builder, Consumer<PagedResponse<T>> consumer);
    <T1, E extends EsScroll> void scrollAll(Class<T1> clazz, E request, EsSearchSourceBuilder<E> builder, Consumer<PagedResponse<T1>> consumer);
}
