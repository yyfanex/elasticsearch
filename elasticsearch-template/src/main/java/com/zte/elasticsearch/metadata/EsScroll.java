package com.zte.elasticsearch.metadata;

import org.apache.commons.lang3.StringUtils;

public interface EsScroll {
    /**
     * 是否开启滚动查询
     */
    default Boolean getScroll() {
        return false;
    }
    void setScroll(Boolean scroll);

    /**
     * 滚动会话有效期
     */
    default Long getDurationSeconds() {
        //默认10分钟
        return 10 * 60L;
    }

    /**
     * 滚动的会话id
     */
    String getScrollId();
    void setScrollId(String scrollId);

    class Default implements EsScroll {
        private Boolean scroll = false;
        private final Long durationSeconds = 10 * 60L;
        private String scrollId = StringUtils.EMPTY;

        @Override
        public Boolean getScroll() {
            return scroll;
        }

        @Override
        public void setScroll(Boolean scroll) {
            this.scroll = scroll;
        }

        @Override
        public Long getDurationSeconds() {
            return durationSeconds;
        }

        @Override
        public String getScrollId() {
            return scrollId;
        }

        @Override
        public void setScrollId(String scrollId) {
            this.scrollId = scrollId;
        }
    }
}
