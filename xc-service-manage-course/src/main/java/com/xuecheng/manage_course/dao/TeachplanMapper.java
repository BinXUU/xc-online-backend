package com.xuecheng.manage_course.dao;

import com.xuecheng.framework.domain.course.ext.TeachplanNode;
import org.apache.ibatis.annotations.Mapper;

/**
 * Created by BinXU on 2020/02/02.
 */
@Mapper
public interface TeachplanMapper {
    public TeachplanNode selectList(String courseId);
}
