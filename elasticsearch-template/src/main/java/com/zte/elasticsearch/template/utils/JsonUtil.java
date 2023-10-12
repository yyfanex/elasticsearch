package com.zte.elasticsearch.template.utils;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.util.Map;

public class JsonUtil {
    private static ObjectMapper objectMapper;

    static {
        objectMapper = new ObjectMapper();
        objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES,false);
    }

    public static <T> String serialize(T obj) {
        try {
            return objectMapper.writeValueAsString(obj);
        }
        catch (Exception ex) {
            throw new RuntimeException(ex);
        }
    }

    public static <T> T deserialize(String strData, Class<T> clazz) {
        try {
            T ret = objectMapper.readValue(strData, clazz);
            return ret;
        }
        catch (Exception ex) {
            throw new RuntimeException(ex);
        }
    }

    public static <T> T mapToBean(Map<String, Object> map, Class<T> clazz) {
        return objectMapper.convertValue(map, clazz);
    }

    public static <T> Map<String, Object> beanToMap(T obj) {
        try {
            return objectMapper.readValue(serialize(obj), Map.class);
        }
        catch (Exception ex) {
            throw new RuntimeException(ex);
        }
    }
}
