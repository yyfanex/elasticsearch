package io.github.yyfanex.template;

import com.zte.elasticsearch.template.Configuration;
import com.zte.elasticsearch.utils.MapUtils;
import io.github.yyfanex.HighLevelClientFactory;
import io.github.yyfanex.entity.Student;
import io.github.yyfanex.mapper.StudentMapper;
import org.apache.http.HttpHost;
import org.apache.http.util.Asserts;
import org.elasticsearch.client.RestClient;
import org.elasticsearch.client.RestClientBuilder;
import org.elasticsearch.client.RestHighLevelClient;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

public class EsTemplateSample {
    public static void main(String[] args) {
        Configuration configuration = Configuration.build().esClient(HighLevelClientFactory.build().newClient());
        StudentMapper studentMapper = new StudentMapper(configuration);
        //Initialized Data
        String kingMobName = "KingMob";
        Student kingMob = Student.builder()
                .name(kingMobName)
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
        //IndexMapper sample
        studentMapper.index(kingMob);
        studentMapper.bulkIndex(Arrays.asList(selcarpa, linHuiG));

        //GetMapper sample
        Optional<Student> getResponse1 = studentMapper.get(kingMobName);
        System.out.println(getResponse1.get());
        Optional<Student> getResponse2 = studentMapper.get(kingMob);
        System.out.println(getResponse2.get());
        List<Student> multipleGetResponse = studentMapper.multipleGet(Collections.singletonList(kingMobName));
        System.out.println(multipleGetResponse.get(0));

        //UpdateMapper sample
        String script = "ctx._source.sex = params.sex;";
        linHuiG.setSex(0);
        studentMapper.update(linHuiG, "ctx._source.sex = params.sex;");
        studentMapper.update(linHuiG.primaryKey(), MapUtils.asMap("sex", 1), script);
        studentMapper.bulkUpdate(Arrays.asList(selcarpa, linHuiG), "ctx._source.sex = params.sex;");

        //DeleteMapper sample
        studentMapper.delete(kingMobName);
        studentMapper.delete(selcarpa);
        studentMapper.bulkDelete(Arrays.asList(linHuiG.primaryKey()));
    }

}
