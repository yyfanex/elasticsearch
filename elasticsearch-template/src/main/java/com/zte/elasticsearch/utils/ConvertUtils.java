package com.zte.elasticsearch.utils;

import com.zte.elasticsearch.metadata.EsConcurrent;
import com.zte.elasticsearch.metadata.EsScore;
import com.zte.elasticsearch.support.JsonMapper;
import org.apache.commons.collections4.MapUtils;
import org.elasticsearch.action.get.GetResponse;
import org.elasticsearch.action.get.MultiGetItemResponse;
import org.elasticsearch.action.get.MultiGetResponse;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.search.SearchHit;
import org.elasticsearch.search.fetch.subphase.highlight.HighlightField;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;

public class ConvertUtils {
    /**
     * 查询结果转dto对象
     */
    public static <T> List<T> convert(JsonMapper jsonMapper, SearchResponse searchResponse, Class<T> clazz) {
        if (searchResponse == null) {
            return Collections.EMPTY_LIST;
        }
        List<T> list = new ArrayList<>(searchResponse.getHits().getHits().length);
        for (SearchHit hit : searchResponse.getHits().getHits()) {
            T entity = jsonMapper.bean(clazz, hit.getSourceAsString());
            if (EsScore.class.isAssignableFrom(clazz)) {
                ((EsScore) entity).setScore(hit.getScore());
            }
            if (EsConcurrent.class.isAssignableFrom(clazz)) {
                ((EsConcurrent) entity).setSeqNo(hit.getSeqNo());
                ((EsConcurrent) entity).setPrimaryTerm(hit.getPrimaryTerm());
            }
            convertHighlight(clazz, entity, hit);
            list.add(entity);
        }
        return list;
    }

    public static <T> T convert(JsonMapper jsonMapper, GetResponse getResponse, Class<T> clazz) {
        if (!getResponse.isExists()) {
            throw new RuntimeException(String.format("not found index document! index:%s, id:%s", getResponse.getIndex(), getResponse.getId()));
        }
        T entity = jsonMapper.bean(clazz, getResponse.getSourceAsString());
        if (EsConcurrent.class.isAssignableFrom(clazz)) {
            ((EsConcurrent) entity).setSeqNo(getResponse.getSeqNo());
            ((EsConcurrent) entity).setPrimaryTerm(getResponse.getPrimaryTerm());
        }
        return entity;
    }

    public static <T> List<T> convert(JsonMapper jsonMapper, MultiGetResponse multiGetResponse, Class<T> clazz) {
        if (multiGetResponse == null) {
            return Collections.EMPTY_LIST;
        }
        List<T> list = new ArrayList<>();
        for (MultiGetItemResponse response : multiGetResponse.getResponses()) {
            if (response.getFailure() != null) {
                throw new RuntimeException(response.getFailure().getMessage());
            }
            T entity = convert(jsonMapper, response.getResponse(), clazz);
            if (entity != null) {
                list.add(entity);
            }
        }
        return list;
    }

    /**
     * 转换高亮结果
     *
     * @param clazz
     * @param entity
     * @param hit
     * @param <T>
     */
    private static <T> void convertHighlight(Class<T> clazz, T entity, SearchHit hit) {
        try {
            if (MapUtils.isNotEmpty(hit.getHighlightFields())) {
                for (Map.Entry<String, HighlightField> highlight : hit.getHighlightFields().entrySet()) {
                    if (highlight.getValue().getFragments().length > 0) {
                        Field highlightField = clazz.getDeclaredField(String.format("%s_highlight", highlight.getKey()));
                        if (highlightField != null) {
                            highlightField.setAccessible(true);
                            highlightField.set(entity, highlight.getValue().getFragments()[0].toString());
                        }
                    }
                }
            }
        } catch (Exception ex) {
            //
        }
    }
}
