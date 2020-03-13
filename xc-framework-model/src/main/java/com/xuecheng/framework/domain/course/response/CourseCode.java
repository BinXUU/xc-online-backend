package com.xuecheng.framework.domain.course.response;

import com.google.common.collect.ImmutableMap;
import com.xuecheng.framework.model.response.ResultCode;
import io.swagger.annotations.ApiModelProperty;
import lombok.ToString;


/**
 * Created by BinXU on 2020/01/22.
 */
@ToString
public enum CourseCode implements ResultCode {
    COURSE_DENIED_DELETE(false,31001,"删除课程失败，只允许删除本机构的课程！"),
    COURSE_PUBLISH_PERVIEWISNULL(false,31002,"还没有进行课程预览！"),
    COURSE_PUBLISH_CDETAILERROR(false,31003,"创建课程详情页面出错！"),
    COURSE_PUBLISH_COURSEIDISNULL(false,31004,"课程Id为空！"),
    COURSE_PUBLISH_COURSEISNULL(false,31005,"课程不存在！"),
    COURSE_PUBLISH_VIEWERROR(false,31006,"发布课程视图出错！"),
    COURSE_MEDIS_URLISNULL(false,31107,"选择的媒资文件访问地址为空！"),
    COURSE_MEDIS_NAMEISNULL(false,31108,"选择的媒资文件名称为空！"),
    COURSE_GET_NOTEXISTS(false,31109,"选择的媒资文件名称为空！"),
    COURSE_MEDIA_TEACHPLAN_ISNULL(false,31111,"选择的媒资文件不存在！"),
    COURSE_MEDIA_TEACHPLAN_GRADEERROR(false,31112,"选择的媒资文件等级错误！"),
    COURSE_PUBLISH_CREATE_INDEX_ERROR(false,31110,"创建课程索引信息失败！");

    //操作代码
    @ApiModelProperty(value = "操作是否成功", example = "true", required = true)
    boolean success;

    //操作代码
    @ApiModelProperty(value = "操作代码", example = "22001", required = true)
    int code;
    //提示信息
    @ApiModelProperty(value = "操作提示", example = "操作过于频繁！", required = true)
    String message;
    private CourseCode(boolean success, int code, String message){
        this.success = success;
        this.code = code;
        this.message = message;
    }
    private static final ImmutableMap<Integer, CourseCode> CACHE;

    static {
        final ImmutableMap.Builder<Integer, CourseCode> builder = ImmutableMap.builder();
        for (CourseCode commonCode : values()) {
            builder.put(commonCode.code(), commonCode);
        }
        CACHE = builder.build();
    }

    @Override
    public boolean success() {
        return success;
    }

    @Override
    public int code() {
        return code;
    }

    @Override
    public String message() {
        return message;
    }
}
