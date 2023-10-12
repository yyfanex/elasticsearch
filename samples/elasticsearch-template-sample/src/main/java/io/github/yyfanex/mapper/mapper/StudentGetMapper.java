package io.github.yyfanex.mapper.mapper;

import com.zte.elasticsearch.template.template.Configuration;
import com.zte.elasticsearch.template.template.EsGetTemplate;
import io.github.yyfanex.mapper.entity.Student;

public class StudentGetMapper extends EsGetTemplate<Student> {
    public StudentGetMapper(Configuration configuration) {
        super(configuration);
    }
}

