package io.github.yyfanex.entity;

import com.zte.elasticsearch.metadata.EsKey;
import lombok.*;

import javax.persistence.Table;

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
