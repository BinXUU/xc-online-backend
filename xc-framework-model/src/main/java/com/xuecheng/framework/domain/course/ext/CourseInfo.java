package com.xuecheng.framework.domain.course.ext;

import com.xuecheng.framework.domain.course.CourseBase;
import lombok.Data;
import lombok.ToString;

/**
 * Created by BinXU on 2020/01/22.
 */
@Data
@ToString
public class CourseInfo extends CourseBase {

    //课程图片
    private String pic;

    //课程名称
    private String name;

    //课程id
    private String id;

}
