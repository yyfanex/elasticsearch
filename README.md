# yyfanex elasticsearch

[English Documentation](README-EN.md) | 中文文档

----------------------------------------

`yyfanex elasticsearch` 库包含了一系列与elasticsearch（简称ES）相关的通用工具，包括ES操作的高级抽象模板、ES与其他存储中间件的同步器等。
其包含了多个模块，包括：
* elasticsearch-template
* elasticsearch-sync（待实现）
* elasticsearch-mapper（待实现）

## 特性

1. AbstractEsTemplate相关对象实现了ES操作的高级抽象，提供模板化操作接口。


## 入门

### elasticsearch-template

添加Maven依赖：

[source,xml]
----
<dependency>
    <groupId>io.github.yyfanex</groupId>
    <artifactId>elasticsearch-template</artifactId>
    <version>7.9.2</version>
</dependency>

----

以下是一个在Java中使用elasticsearch-template的代码示例：

[source,java]
----

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

public class HighLevelClientFactory {
    public static HighLevelClientFactory build() {
        return new HighLevelClientFactory();
    }

    public RestHighLevelClient newClient() {
        RestClientBuilder clientBuilder = RestClient.builder(HttpHost.create("10.54.147.135:9204"));
        RestHighLevelClient restHighLevelClient = new RestHighLevelClient(clientBuilder);
        return restHighLevelClient;
    }
}

@Table(name = "student")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@ToString
public class Student implements EsKey {
    private String name;
    private String classes;
    private Integer sex;
    public String primaryKey() {
        return name;
    }
}

----

## 帮助

有问题吗？我很乐意提供帮助！

* 邮件咨询：fan2020@126.com


## Links

[search.maven.org](https://central.sonatype.com/namespace/io.github.yyfanex) 