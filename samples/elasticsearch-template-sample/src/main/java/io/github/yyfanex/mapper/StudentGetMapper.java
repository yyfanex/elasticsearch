package io.github.yyfanex.mapper;

import com.zte.elasticsearch.template.Configuration;
import com.zte.elasticsearch.template.EsBaseTemplate;
import com.zte.elasticsearch.template.EsGetTemplate;
import io.github.yyfanex.entity.Student;

public class StudentGetMapper extends EsGetTemplate<Student> {
    public StudentGetMapper(Configuration configuration) {
        super(configuration);
    }
}

