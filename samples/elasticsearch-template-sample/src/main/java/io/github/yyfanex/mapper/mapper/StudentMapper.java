package io.github.yyfanex.mapper.mapper;

import com.zte.elasticsearch.template.template.Configuration;
import com.zte.elasticsearch.template.template.EsBaseTemplate;
import io.github.yyfanex.mapper.entity.Student;

public class StudentMapper extends EsBaseTemplate<Student> {
    public StudentMapper(Configuration configuration) {
        super(configuration);
    }
}
