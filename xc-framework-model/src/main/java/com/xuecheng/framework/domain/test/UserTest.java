package com.xuecheng.framework.domain.test;

import lombok.Data;
import lombok.ToString;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import javax.persistence.Column;
import java.util.Date;

/**
 * Created by BinXU on 2020/01/22.
 */
@Data
@ToString
@Document(collection = "user_test")
public class UserTest {


    @Id
    private String id;
    private String name;

    @Column(name="create_time")
    private Date createTime;
}
