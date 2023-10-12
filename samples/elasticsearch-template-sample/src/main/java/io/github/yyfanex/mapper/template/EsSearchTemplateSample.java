package io.github.yyfanex.mapper.template;

import com.zte.elasticsearch.template.entity.PagedResponse;
import com.zte.elasticsearch.template.metadata.EsScroll;
import com.zte.elasticsearch.template.template.Configuration;
import com.zte.elasticsearch.template.template.EsSearchTemplate;
import io.github.yyfanex.mapper.HighLevelClientFactory;
import io.github.yyfanex.mapper.entity.Student;
import io.github.yyfanex.mapper.mapper.StudentMapper;
import lombok.Builder;
import lombok.Data;
import org.apache.commons.lang3.StringUtils;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.index.query.BoolQueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.search.aggregations.AggregationBuilder;
import org.elasticsearch.search.aggregations.AggregationBuilders;
import org.elasticsearch.search.aggregations.bucket.terms.ParsedLongTerms;
import org.elasticsearch.search.aggregations.bucket.terms.Terms;
import org.elasticsearch.search.aggregations.metrics.ParsedValueCount;
import org.elasticsearch.search.builder.SearchSourceBuilder;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

public class EsSearchTemplateSample {
    public static void main(String[] args) {
        Configuration configuration = Configuration.build().esClient(HighLevelClientFactory.build().newClient());
        List<Student> students = prepareData();
        initialize(configuration, students);
        try {
            EsSearchTemplate<Student> searchTemplate = new EsSearchTemplate<>(Student.class, configuration);

            PagedResponse<Student> pagedResponse = searchTemplate.search(new SearchSourceBuilder().query(QueryBuilders.termQuery("sex", 1)));
            System.out.println(String.format("[search1] result size:%d", pagedResponse.getBo().size()));

            pagedResponse = searchTemplate.search(SearchRequest.builder().classes("2.3").build(), entity -> entity.searchSource());
            System.out.println(String.format("[search2] result size:%d", pagedResponse.getBo().size()));

            //SELECT sex,count(name) score FROM student group by sex
            AggregationBuilder groupBySex = AggregationBuilders.terms("sex").field("sex").size(100);
            AggregationBuilder countName = AggregationBuilders.count("count").field("name.keyword");
            PagedResponse<CountBySexResponse> countBySexResponse = searchTemplate.search(new SearchSourceBuilder().aggregation(groupBySex.subAggregation(countName)), CountBySexResponse.class,
                    (jsonMapper, searchResponse, clazz) -> EsSearchTemplateSample.convert(searchResponse));
            System.out.println(String.format("[search3] result size:%d", countBySexResponse.getBo().size()));

            pagedResponse = searchTemplate.scrollSearch(ScrollSearchRequest.builder().scroll(true).sex(1).build(), entity -> entity.searchSource());
            System.out.println(String.format("[scrollSearch1] result size:%d scrollId:%s", pagedResponse.getBo().size(), pagedResponse.getScrollId()));

            List<Student> scrollAllResult = new ArrayList<>();
            searchTemplate.scrollAll(ScrollSearchRequest.builder().scroll(true).sex(1).build(), entity -> entity.searchSource(),
                    studentPagedResponse -> scrollAllResult.addAll(studentPagedResponse.getBo()));
            System.out.println(String.format("[scrollSearch2] result size:%d", scrollAllResult.size()));
        }
        finally {
            preDestroy(configuration, students);
        }
    }

    private static List<Student> prepareData() {
        Student kingMob = Student.builder()
                .name("KingMob")
                .sex(1)
                .classes("4.3")
                .build();
        Student selcarpa = Student.builder()
                .name("selcarpa")
                .sex(0)
                .classes("2.3")
                .build();
        Student linHuiG = Student.builder()
                .name("LinHuiG")
                .sex(1)
                .classes("2.3")
                .build();
        return Arrays.asList(kingMob, selcarpa, linHuiG);
    }

    private static void initialize(Configuration configuration, List<Student> students) {
        StudentMapper studentMapper = new StudentMapper(configuration);
        studentMapper.bulkIndex(students);
    }

    private static void preDestroy(Configuration configuration, List<Student> students) {
        StudentMapper studentMapper = new StudentMapper(configuration);
        studentMapper.bulkDelete(students.stream().map(student -> student.primaryKey()).collect(Collectors.toList()));
    }

    private static List<CountBySexResponse> convert(SearchResponse searchResponse) {
        if (searchResponse == null) {
            return Collections.EMPTY_LIST;
        }
        List<CountBySexResponse> list = new ArrayList<>();
        ParsedLongTerms dimension = searchResponse.getAggregations().get("sex");
        for (Terms.Bucket bucket : dimension.getBuckets()) {
            CountBySexResponse response = CountBySexResponse.builder()
                    .sex(((Long)bucket.getKey()).intValue())
                    .count((int) ((ParsedValueCount)bucket.getAggregations().get("count")).getValue())
                    .build();
            list.add(response);
        }
        return list;
    }

    @Data
    @Builder
    static class ScrollSearchRequest implements EsScroll {
        private Integer sex;
        private Boolean scroll;
        private String scrollId;
        public SearchSourceBuilder searchSource() {
            return new SearchSourceBuilder()
                    .query(QueryBuilders.termQuery("sex", sex))
                    .size(1);
        }
    }

    @Data
    @Builder
    static class CountBySexResponse {
        private Integer sex;
        private Integer count;
    }

    @Data
    @Builder
    static class SearchRequest {
        private String name;
        private String classes;

        public SearchSourceBuilder searchSource() {
            BoolQueryBuilder query = QueryBuilders.boolQuery();
            if (StringUtils.isNotBlank(name)) {
                query.must(QueryBuilders.matchQuery("name", name));
            }
            if (StringUtils.isNotBlank(classes)) {
                query.must(QueryBuilders.termQuery("classes.keyword", classes));
            }
            SearchSourceBuilder sourceBuilder = new SearchSourceBuilder()
                    .query(query)
                    .size(10);
            return sourceBuilder;
        }
    }
}
