package com.xuecheng.ossfilesystem.dao;

import com.xuecheng.framework.domain.filesystem.FileSystem;
import org.springframework.data.mongodb.repository.MongoRepository;

/**
 * Created by BinXU.
 */
public interface FileSystemRepository extends MongoRepository<FileSystem,String> {
}
