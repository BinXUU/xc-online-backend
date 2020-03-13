package com.xuecheng.manage_cms_client.dao;

import com.xuecheng.framework.domain.cms.CmsSite;
import org.springframework.data.mongodb.repository.MongoRepository;

/**
 * Created by BinXU on 2020/01/30.
 */
public interface CmsSiteRepository extends MongoRepository<CmsSite, String> {
}
