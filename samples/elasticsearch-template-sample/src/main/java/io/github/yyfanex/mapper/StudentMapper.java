package io.github.yyfanex.mapper;

import com.zte.elasticsearch.template.Configuration;
import com.zte.elasticsearch.template.EsBaseTemplate;
import io.github.yyfanex.entity.Student;

public class StudentMapper extends EsBaseTemplate<Student> {
    public StudentMapper(Configuration configuration) {
        super(configuration);
    }
}
