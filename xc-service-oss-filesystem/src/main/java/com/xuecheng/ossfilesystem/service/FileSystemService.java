package com.xuecheng.ossfilesystem.service;

import com.alibaba.fastjson.JSON;
import com.aliyun.oss.OSSClient;
import com.xuecheng.ossfilesystem.config.AliyunConfig;
import com.xuecheng.framework.domain.filesystem.FileSystem;
import com.xuecheng.framework.domain.filesystem.response.FileSystemCode;
import com.xuecheng.framework.domain.filesystem.response.UploadFileResult;
import com.xuecheng.framework.exception.ExceptionCast;
import com.xuecheng.framework.model.response.CommonCode;
import com.xuecheng.ossfilesystem.dao.FileSystemRepository;
import org.apache.commons.lang3.RandomUtils;
import org.apache.commons.lang3.StringUtils;
import org.joda.time.DateTime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.ByteArrayInputStream;
import java.util.Map;

/**
 * @author BinXU
 * @version 1.0
 **/
@Service
public class FileSystemService {

    // 允许上传的格式,目前只支持上传图片功能
    private static final String[] IMAGE_TYPE = new String[]{".bmp", ".jpg",
            ".jpeg", ".gif", ".png"};

    @Autowired
    private AliyunConfig aliyunConfig;
//    private OSSClient ossClient;

//    @Autowired
//    private AliyunConfig aliyunConfig;

    @Autowired
    FileSystemRepository fileSystemRepository;

    //上传文件
    public UploadFileResult upload( MultipartFile multipartFile,
                                   String filetag,
                                   String businesskey,
                                   String metadata){
        if(multipartFile ==null){
            ExceptionCast.cast(FileSystemCode.FS_UPLOADFILE_FILEISNULL);
        }
        //第一步：将文件上传到oss中，得到一个文件id
        String filePath = oss_upload(multipartFile);
        System.out.println(filePath+"\n");
        if(StringUtils.isEmpty(filePath)){
            ExceptionCast.cast(FileSystemCode.FS_UPLOADFILE_SERVERFAIL);
        }
        //第二步：将文件id及其它文件信息存储到mongodb中。
        FileSystem fileSystem = new FileSystem();
        fileSystem.setFileId(String.valueOf(System.currentTimeMillis()));
        fileSystem.setFilePath(filePath);
        fileSystem.setFiletag(filetag);
        fileSystem.setBusinesskey(businesskey);
        fileSystem.setFileName(multipartFile.getOriginalFilename());
        fileSystem.setFileType(multipartFile.getContentType());
        if(StringUtils.isNotEmpty(metadata)){
            try {
                Map map = JSON.parseObject(metadata, Map.class);
                fileSystem.setMetadata(map);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        fileSystemRepository.save(fileSystem);
        return new UploadFileResult(CommonCode.SUCCESS,fileSystem);
    }

    //上传文件到aliyun oss服务器

    /**
     *
     * @param multipartFile 文件
     * @return
     */
    private String oss_upload(MultipartFile uploadFile) {

        //图片做校验，对后缀名
        boolean isLegal = false;

        for (String type : IMAGE_TYPE) {
            if (StringUtils.endsWithIgnoreCase(uploadFile.getOriginalFilename(),
                    type)) {
                isLegal = true;
                break;
            }
        }

        if(!isLegal) ExceptionCast.cast(FileSystemCode.FS_UPLOADFILE_IRLEGALTYPE);

        String fileName = uploadFile.getOriginalFilename();
        String filePath = getImagePath(fileName);
        OSSClient ossClient = new OSSClient(aliyunConfig.getEndpoint(), aliyunConfig.getAccessKeyId(), aliyunConfig.getAccessKeySecret());
        try {
            ossClient.putObject(aliyunConfig.getBucketName(), filePath, new
                    ByteArrayInputStream(uploadFile.getBytes()));
        }catch (Exception e) {
            e.printStackTrace();
        }
        return aliyunConfig.getUrlPrefix()+filePath;
    }

    private String getImagePath(String sourceFileName) {
        DateTime dateTime = new DateTime();
        return "images/" + dateTime.toString("yyyy")
                + "/" + dateTime.toString("MM") + "/"
                + dateTime.toString("dd") + "/" + System.currentTimeMillis() +
                RandomUtils.nextInt(100, 9999) + "." +
                StringUtils.substringAfterLast(sourceFileName, ".");
    }
}
