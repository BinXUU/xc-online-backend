package com.xuecheng.manage_cms_client.dao;

import com.xuecheng.framework.domain.cms.CmsPage;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.MongoRepository;

/**
 * Created by BinXU on 2020/01/30.
 */
public interface CmsPageRepository extends MongoRepository<CmsPage, String> {
}
