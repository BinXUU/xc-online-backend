package com.xuecheng.manage_media.dao;

import com.xuecheng.framework.domain.media.MediaFile;
import org.springframework.data.mongodb.repository.MongoRepository;

/**
 * Created by BinXU on 2020/02/14.
 */
public interface MediaFileDao extends MongoRepository<MediaFile, String> {
}
