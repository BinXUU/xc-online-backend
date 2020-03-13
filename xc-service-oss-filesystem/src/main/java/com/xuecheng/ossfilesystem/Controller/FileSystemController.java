package com.xuecheng.ossfilesystem.Controller;

import com.xuecheng.filesystem.FileSystemControllerApi;
import com.xuecheng.framework.domain.filesystem.response.UploadFileResult;
import com.xuecheng.ossfilesystem.service.FileSystemService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

/**
 * @author BinXU
 * @version 1.0
 **/
@RestController
@RequestMapping("/filesystem")
public class FileSystemController implements FileSystemControllerApi {
    @Autowired
    FileSystemService fileSystemService;


    //这边可太坑了，要求与前端<el-upload>的name对应起来
    @PostMapping("/upload")
    public UploadFileResult upload(@RequestParam("multipartFile")MultipartFile multipartFile, String filetag, String businesskey, String metadata) {

        return fileSystemService.upload(multipartFile, filetag, businesskey, metadata);
    }
}
