package com.xuecheng.manage_cms.dao;

import com.xuecheng.framework.domain.cms.CmsSite;
import org.springframework.data.mongodb.repository.MongoRepository;

/**
 * Created by BinXU on 2020/02/07.
 */
public interface CmsSiteRepository extends MongoRepository<CmsSite,String> {
}
