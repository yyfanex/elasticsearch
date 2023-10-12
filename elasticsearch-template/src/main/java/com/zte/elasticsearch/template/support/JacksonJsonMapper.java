package com.zte.elasticsearch.template.support;

import com.fasterxml.jackson.databind.ObjectMapper;

import java.util.Map;

public class JacksonJsonMapper implements JsonMapper {
    private static ObjectMapper objectMapper = new ObjectMapper();

    @Override
    public String string(Object obj) {
        try {
            return objectMapper.writeValueAsString(obj);
        }
        catch (Exception e) {
            throw new JsonException("Error serializing object.  Cause: " + e, e);
        }
    }

    @Override
    public <T> T bean(Class<T> clazz, String content) {
        try {
            return objectMapper.readValue(content, clazz);
        }
        catch (Exception e) {
            throw new JsonException("Error deserializing object.  Cause: " + e, e);
        }
    }

    @Override
    public Map<String, Object> map(Object obj) {
        try {
            return objectMapper.readValue(string(obj), Map.class);
        }
        catch (Exception e) {
            throw new JsonException("Error converting object.  Cause: " + e, e);
        }
    }
}
