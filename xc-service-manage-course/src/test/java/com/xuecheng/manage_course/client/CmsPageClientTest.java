package com.xuecheng.manage_course.client;

import com.xuecheng.framework.domain.cms.CmsPage;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import static org.junit.Assert.*;

/**
 * Created by BinXU on 2020/02/06.
 */
@SpringBootTest
@RunWith(SpringRunner.class)
public class CmsPageClientTest {

    @Autowired
    CmsPageClient cmsPageClient;

    @Test
    public void findCmsPageById() {
        //通过服务id调用cms的页面查询接口
        CmsPage cmsPage = cmsPageClient.findById("5a754adf6abb500ad05688d9");
        System.out.println(cmsPage);
    }
}