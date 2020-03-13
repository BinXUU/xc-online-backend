package com.xuecheng.framework.domain.cms;

import lombok.Data;
import lombok.ToString;

import java.util.Map;

/**
 * Created by BinXU on 2020/01/22.
 */
@Data
@ToString
public class CmsConfigModel {
    private String key; //主键
    private String name; //项目名称
    private String url; //项目url
    private Map mapValue; //项目复杂值
    private String value; //项目简单值

}
