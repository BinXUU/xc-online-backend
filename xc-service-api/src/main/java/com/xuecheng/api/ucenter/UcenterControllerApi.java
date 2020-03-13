package com.xuecheng.api.ucenter;

import com.xuecheng.framework.domain.ucenter.ext.XcUserExt;
import io.swagger.annotations.Api;

/**
 * Created by BinXU on 2020/02/19.
 */

@Api(value = "用户中心", description = "用户中心管理")
public interface UcenterControllerApi {
    public XcUserExt getUserext(String username);
}
