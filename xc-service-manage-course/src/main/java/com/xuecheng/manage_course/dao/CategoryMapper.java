package com.xuecheng.manage_course.dao;

import com.xuecheng.framework.domain.course.ext.CategoryNode;
import org.apache.ibatis.annotations.Mapper;

/**
 * Created by BinXU on 2020/02/03.
 */
@Mapper
public interface CategoryMapper {
    //查询分类
    public CategoryNode selectList();
}
