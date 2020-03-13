package com.xuecheng.api.cms;

import com.xuecheng.framework.domain.cms.CmsPage;
import com.xuecheng.framework.domain.cms.request.QueryPageRequest;
import com.xuecheng.framework.domain.cms.response.CmsPageResult;
import com.xuecheng.framework.domain.cms.response.CmsPostPageResult;
import com.xuecheng.framework.model.response.QueryResponseResult;
import com.xuecheng.framework.model.response.ResponseResult;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.DeleteMapping;

/**
 * Created by BinXU on 2020/01/23.
 */
@Api(value = "cms页面管理接口", description = "cms页面管理接口，提供页面的增删改查")
public interface CmsPageControllerApi {

    //页面查询
    @ApiOperation("分页查询页面列表")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "page", value = "页码", required = true, paramType = "path", dataType = "int"),
            @ApiImplicitParam(name = "size", value = "每页记录数", required = true, paramType = "path", dataType = "int")
    })
    public QueryResponseResult findList(int page, int size, QueryPageRequest pageRequest);

    //新增页面接口
    @ApiOperation("添加页面")
    public CmsPageResult add(CmsPage cmsPage);

    //根据id查询页面
    @ApiOperation("根据id查询页面")
    public CmsPage findById(String id);

    //根据id更新页面信息
    @ApiOperation("根据id更新页面信息")
    public CmsPageResult edit(String id, CmsPage cmsPage);

    //通过id删除页面
    @ApiOperation("通过id删除页面")
    public ResponseResult delete(String id);

    //发布页面
    @ApiOperation("发布页面")
    public ResponseResult post(String pageId);

    @ApiOperation("保存页面")
    public CmsPageResult save(CmsPage cmsPage);

    @ApiOperation("一键发布页面")
    public CmsPostPageResult postPageQuick(CmsPage cmsPage);

}
