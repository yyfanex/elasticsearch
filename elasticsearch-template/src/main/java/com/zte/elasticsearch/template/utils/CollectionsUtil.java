package com.zte.elasticsearch.template.utils;

import java.util.Collection;

public class CollectionsUtil {
    public static Boolean isEmpty(Collection collection) {
        return collection == null || collection.isEmpty();
    }

    public static Boolean isNotEmpty(Collection collection) {
        return collection != null && collection.size() > 0;
    }

}
