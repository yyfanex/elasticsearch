package com.zte.elasticsearch.utils;

import java.util.HashMap;
import java.util.Map;

public class MapUtils {
    public static boolean isEmpty(final Map<?,?> map) {
        return map == null || map.isEmpty();
    }

    public static boolean isNotEmpty(final Map<?,?> map) {
        return !MapUtils.isEmpty(map);
    }

    public static Map<String, Object> asMap(Object... keyAndValues) {
        Map<String, Object> ret = new HashMap<>(keyAndValues.length);
        String key = null;
        for (int i = 0; i < keyAndValues.length; i++) {
            if (i % 2 == 1) {
                ret.put(key, keyAndValues[i]);
            }
            else {
                key = keyAndValues[i].toString();
            }
        }
        return ret;
    }
}
