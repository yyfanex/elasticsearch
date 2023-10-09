package com.zte.elasticsearch.support;

import java.util.Map;

public interface JsonMapper {
    String string(Object obj);

    <T> T bean(Class<T> clazz, String content);

    Map<String, Object> map(Object obj);
}
