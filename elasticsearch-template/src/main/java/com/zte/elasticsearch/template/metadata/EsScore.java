package com.zte.elasticsearch.template.metadata;

/**
 * elasticsearch 搜索分数
 */
public interface EsScore {
    Float getScore();
    void setScore(Float score);

}

